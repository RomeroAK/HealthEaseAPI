package com.ayanda.HealthEaseApi.service;


import com.ayanda.HealthEaseApi.additional.UserPrincipal;
import com.ayanda.HealthEaseApi.entities.User;
import com.ayanda.HealthEaseApi.repos.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("user not found: " + username)));
        return user.map(UserPrincipal::new).orElse(null);
    }
}
