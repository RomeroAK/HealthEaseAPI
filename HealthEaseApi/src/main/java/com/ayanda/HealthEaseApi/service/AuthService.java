package com.ayanda.HealthEaseApi.service;


import com.ayanda.HealthEaseApi.additional.AuthResponse;
import com.ayanda.HealthEaseApi.additional.LoginRequest;
import com.ayanda.HealthEaseApi.additional.RegisterRequest;
import com.ayanda.HealthEaseApi.additional.UserRole;
import com.ayanda.HealthEaseApi.config.JwtService;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.AuthException;
import com.ayanda.HealthEaseApi.entities.User;
import com.ayanda.HealthEaseApi.repos.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        try {
            // Validate passwords match
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                return AuthResponse.builder()
                        .success(false)
                        .message("Passwords do not match")
                        .build();
            }

            // Check if username already exists
            if (userRepository.findByUserName(request.getUsername()).isPresent()) {
                return AuthResponse.builder()
                        .success(false)
                        .message("Username already exists")
                        .build();
            }

            // Check if email already exists
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return AuthResponse.builder()
                        .success(false)
                        .message("Email already exists")
                        .build();
            }

            // Create new user
            User user = User.builder()
                    .userName(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getUserRole())
                    .isActive(true)
                    .profileCompleted(false)
                    .build();

            User savedUser = userRepository.save(user);
            log.info("New user registered: {}", savedUser.getEmail());

            return AuthResponse.builder()
                    .success(true)
                    .message("User registered successfully")
                    .user(savedUser)
                    .build();

        } catch (Exception e) {
            log.error("Error during user registration", e);
            return AuthResponse.builder()
                    .success(false)
                    .message("Registration failed. Please try again.")
                    .build();
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Get user details
//            User user = userRepository.findByUserNameAndPassword(request.getUsername(), request.getPassword())
//                    .orElseThrow(() -> new AuthException("User not found"));

            User user = userRepository.findByUserName(request.getUsername()).orElseThrow(() -> new AuthException("User not found"));

            if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
                throw new AuthException("Incorrect Password!");
            }

            // Check if user is active
            if (!user.isActive()) {
                return AuthResponse.builder()
                        .success(false)
                        .message("Account is deactivated. Please contact support.")
                        .build();
            }

            // Generate JWT token
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(user.getUserName())
                    .password(user.getPassword())
                    .authorities(user.getRole().name())
                    .build();
            String jwtToken = jwtService.generateToken(userDetails);
            long expirationTime = jwtService.getExpirationTime();

            log.info("User logged in: {}", user.getUserName());

            String redirectTo = null;

            if(user.getRole().equals(UserRole.PATIENT)){
                if(user.isProfileCompleted()){
                    redirectTo = "patient-dashboard";
                }else{
                    redirectTo = "patient-profile-setup";
                }
            } else if(user.getRole().equals(UserRole.DOCTOR)){
                if(user.isProfileCompleted()){
                    redirectTo = "doctor-dashboard";
                }else{
                    redirectTo = "doctor-profile-setup";
                }
            }

            return AuthResponse.builder()
                    .success(true)
                    .message("Login successful")
                    .user(user)
                    .redirectTo(redirectTo)
                    .token(jwtToken)
                    .expiresIn(expirationTime / 1000) // Convert to seconds
                    .build();

        } catch (Exception e) {
            log.error("Error during login for user: {}", request.getUsername(), e);
            return AuthResponse.builder()
                    .success(false)
                    .message("Invalid username or password")
                    .build();
        }
    }

    public AuthResponse refreshToken(String refreshToken) {
        try {
            // Extract username from refresh token
            String username = jwtService.extractUsername(refreshToken);


            if (username != null) {
                User user = userRepository.findByUserName(username)
                        .orElseThrow(() -> new AuthException("User not found"));

                if (jwtService.isTokenValid(refreshToken, (UserDetails) user)) {
                    String newToken = jwtService.generateToken((UserDetails) user);
                    long expirationTime = jwtService.getExpirationTime();

                    return AuthResponse.builder()
                            .success(true)
                            .message("Token refreshed successfully")
                            .token(newToken)
                            .expiresIn(expirationTime / 1000)
                            .build();
                }
            }

            return AuthResponse.builder()
                    .success(false)
                    .message("Invalid refresh token")
                    .build();

        } catch (Exception e) {
            log.error("Error refreshing token", e);
            return AuthResponse.builder()
                    .success(false)
                    .message("Token refresh failed")
                    .build();
        }
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUserName(username);
    }
}
