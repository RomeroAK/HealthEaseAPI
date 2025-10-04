package com.ayanda.HealthEaseApi.controller;


import com.ayanda.HealthEaseApi.additional.CurrentUser;
import com.ayanda.HealthEaseApi.additional.UserPrincipal;
import com.ayanda.HealthEaseApi.dto.dtoObjects.ApiResponseDto;

import com.ayanda.HealthEaseApi.dto.dtoObjects.CompleteProfileUpdateDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.PatientProfileResponseDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.DoctorProfileResponseDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.ResourceNotFoundException;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.UnauthorizedException;
import com.ayanda.HealthEaseApi.service.DoctorProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// DoctorController.java
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DoctorProfileController {
    private final DoctorProfileService doctorProfileService;


    @GetMapping("{userId}/doctors/profiles/get-all")
    public ResponseEntity<ApiResponseDto> getAllDoctors(@PathVariable Long userId) {

        try {
            List<DoctorProfileResponseDto> doctors = doctorProfileService.getAllDoctors();
            ApiResponseDto response = new ApiResponseDto(true, "Doctors retrieved successfully", doctors);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(false, "Failed to retrieve profile: " + e.getMessage()));
        }
    }

    @GetMapping("{userId}/doctor/profiles/get-by-id")
    public ResponseEntity<ApiResponseDto> getProfileByUserId(@PathVariable Long userId) {
        try {
            DoctorProfileResponseDto doctor = doctorProfileService.getDoctorProfile(userId);
            ApiResponseDto response = new ApiResponseDto(true, "Doctor profile retrieved successfully", doctor);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(false, "Failed to retrieve profile: " + e.getMessage()));
        }
    }

    @PutMapping("{userId}/doctor/profiles/update")
    @PreAuthorize("hasRole('doctor') or hasRole('admin')")
    public ResponseEntity<ApiResponseDto> updateDoctorProfile(@PathVariable Long userId, @Valid @RequestBody CompleteProfileUpdateDto profileData) {
        try {
            DoctorProfileResponseDto updatedDoctor = doctorProfileService.updateDoctorProfile(userId, profileData);
            ApiResponseDto response = new ApiResponseDto(true, "Doctor profile updated successfully", updatedDoctor);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(false, "Failed to update profile: " + e.getMessage()));
        }
    }

    @GetMapping("{userId}/doctors/profiles/search-by-name-surname")
    public ResponseEntity<ApiResponseDto> getDoctorsByNameAndSurname(@RequestParam String name, @RequestParam String surname) {
        try {
            List<DoctorProfileResponseDto> doctors = doctorProfileService.getDoctorsByNameAndSurname(name, surname);
            ApiResponseDto response = new ApiResponseDto(true, "Doctors retrieved successfully", doctors);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(false, "Failed to retrieve doctors: " + e.getMessage()));
        }
    }

    @GetMapping("{userId}/doctors/profiles/search-by-specialization/{specialization}")
    public ResponseEntity<ApiResponseDto> getDoctorsBySpecialization(@PathVariable String specialization) {
        try {
            List<DoctorProfileResponseDto> doctors = doctorProfileService.getDoctorsBySpecialization(specialization);
            ApiResponseDto response = new ApiResponseDto(true, "Doctors retrieved successfully", doctors);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(false, "Failed to retrieve doctors: " + e.getMessage()));
        }
    }

    @GetMapping("{userId}/doctors/profiles/get-linked-patients")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> getAllPatientsLinkedToDoctor(@PathVariable Long userId, @CurrentUser UserPrincipal currentUser) {
        try {
            List<PatientProfileResponseDto> patients = doctorProfileService.getPatientsLinkedToDoctor(userId);
            ApiResponseDto response = new ApiResponseDto(true, "Patients retrieved successfully", patients);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(false, "Failed to retrieve patients: " + e.getMessage()));
        }
    }


}
