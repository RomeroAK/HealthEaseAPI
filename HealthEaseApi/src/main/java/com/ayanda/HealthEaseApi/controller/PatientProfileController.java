package com.ayanda.HealthEaseApi.controller;


import com.ayanda.HealthEaseApi.additional.CurrentUser;
import com.ayanda.HealthEaseApi.additional.UserPrincipal;
import com.ayanda.HealthEaseApi.dto.dtoObjects.*;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.DoctorProfileResponseDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.ResourceNotFoundException;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.UnauthorizedException;
import com.ayanda.HealthEaseApi.dto.dtoObjects.request.IdValidationRequestDto;
import com.ayanda.HealthEaseApi.service.PatientProfileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientProfileController {

    @Autowired
    private PatientProfileService patientProfileService;

    /**
     * Get complete patient profile
     * GET /api/patients/{userId}/profile
     */
    @GetMapping("/{userId}/patient/profile")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> getProfile(
            @PathVariable Long userId) {
        try {

            PatientProfileResponseDto profile = patientProfileService.getPatientProfile(userId);

            return ResponseEntity.ok(
                    new ApiResponseDto(true, "Profile retrieved successfully", profile)
            );
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

    /**
     * Get patient profile completion status
     * GET /api/patients/{userId}/profile/completion
     */
    @GetMapping("/{userId}/patient/profile/completion")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> getProfileCompletion(
            @PathVariable @NotNull Long userId,
            @CurrentUser UserPrincipal currentUser) {
        try {
            validateUserAccess(userId, currentUser);

            ProfileCompletionDto completion = patientProfileService.getProfileCompletion(userId);

            return ResponseEntity.ok(
                    new ApiResponseDto(true, "Profile completion retrieved successfully", completion)
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(false, "Failed to retrieve completion status"));
        }
    }

    /**
     * Get enhanced profile completion with insights
     * GET /api/patients/{userId}/profile/completion/detailed
     */
    @GetMapping("/{userId}/patient/profile/completion/detailed")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> getDetailedProfileCompletion(
            @PathVariable @NotNull Long userId,
            @CurrentUser UserPrincipal currentUser) {
        try {
            validateUserAccess(userId, currentUser);

            ProfileCompletionDto completion = patientProfileService.getProfileCompletion(userId);

            return ResponseEntity.ok(
                    new ApiResponseDto(true, "Detailed completion retrieved successfully", completion)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(false, "Failed to retrieve detailed completion"));
        }
    }

    // =============================================
    // COMPLETE PROFILE UPDATE ENDPOINTS
    // ========================================
    /**
     * Update complete profile (JSON only, no file upload)
     * PUT /api/patients/{userId}/profile/data
     */
    @PutMapping(value = "/{userId}/patient/profile")
    @PreAuthorize("hasRole('patient') or hasRole('admin')")
    public ResponseEntity<ApiResponseDto> updateProfileData(
            @PathVariable(name = "userId") @NotNull Long userId,
            @Valid @RequestBody CompleteProfileUpdateDto profileData) {
        try {

            PatientProfileResponseDto updatedProfile = patientProfileService.updateCompleteProfile(
                    userId, profileData, null);

            return ResponseEntity.ok(
                    new ApiResponseDto(true, "Profile data updated successfully", updatedProfile)
            );
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto(false, "Failed to update profile: " + e.getMessage()));
        }
    }

    // =============================================
    // SECTION-SPECIFIC UPDATE ENDPOINTS
    // =============================================

    /**
     * Update personal information only
     * PUT /api/patients/{userId}/profile/personal
     */
    @PutMapping("/{userId}/profile/personal")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> updatePersonalInfo(
            @PathVariable @NotNull Long userId,
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody PersonalInfoUpdateDto personalInfo) {
        try {
            validateUserAccess(userId, currentUser);

            PatientProfileResponseDto updatedProfile = patientProfileService.updatePersonalInfo(
                    userId, personalInfo);

            return ResponseEntity.ok(
                    new ApiResponseDto(true, "Personal information updated successfully", updatedProfile)
            );
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto(false, "Failed to update personal info: " + e.getMessage()));
        }
    }

    /**
     * Update medical information only
     * PUT /api/patients/{userId}/profile/medical
     */
    @PutMapping("/{userId}/profile/medical")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> updateMedicalInfo(
            @PathVariable @NotNull Long userId,
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody MedicalInfoUpdateDto medicalInfo) {
        try {
            validateUserAccess(userId, currentUser);

            // Validate medical data
            patientProfileService.validateMedicalData(medicalInfo);

            PatientProfileResponseDto updatedProfile = patientProfileService.updateMedicalInfo(
                    userId, medicalInfo);

            return ResponseEntity.ok(
                    new ApiResponseDto(true, "Medical information updated successfully", updatedProfile)
            );
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto(false, "Invalid medical data: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto(false, "Failed to update medical info: " + e.getMessage()));
        }
    }

    /**
     * Update emergency contacts only
     * PUT /api/patients/{userId}/profile/emergency-contacts
     */
    @PutMapping("/{userId}/profile/emergency-contacts")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> updateEmergencyContacts(
            @PathVariable @NotNull Long userId,
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody EmergencyContactsUpdateDto emergencyContacts) {
        try {
            validateUserAccess(userId, currentUser);

            PatientProfileResponseDto updatedProfile = patientProfileService.updateEmergencyContacts(
                    userId, emergencyContacts);

            return ResponseEntity.ok(
                    new ApiResponseDto(true, "Emergency contacts updated successfully", updatedProfile)
            );
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto(false, "Failed to update emergency contacts: " + e.getMessage()));
        }
    }

    /**
     * Update insurance information only
     * PUT /api/patients/{userId}/profile/insurance
     */
    @PutMapping("/{userId}/profile/insurance")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> updateInsuranceInfo(
            @PathVariable @NotNull Long userId,
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody InsuranceInfoUpdateDto insuranceInfo) {
        try {
            validateUserAccess(userId, currentUser);

            PatientProfileResponseDto updatedProfile = patientProfileService.updateInsuranceInfo(
                    userId, insuranceInfo);

            return ResponseEntity.ok(
                    new ApiResponseDto(true, "Insurance information updated successfully", updatedProfile)
            );
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto(false, "Failed to update insurance info: " + e.getMessage()));
        }
    }

    @GetMapping("/{userId}/patient/get/my-doctors")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> getDoctorsLinkedToPatient( @PathVariable @NotNull Long userId,
                                                                     @CurrentUser UserPrincipal currentUser){
        try {

            validateUserAccess(userId, currentUser);
            List<DoctorProfileResponseDto> doctors = patientProfileService.getAllDoctorsLinkedToPatient(userId);

            return ResponseEntity.ok(
                    new ApiResponseDto(true, "Doctors retrieved successfully", doctors)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto(false, "Failed to retrieve doctors: " + e.getMessage()));
        }
    }

    /**
     * Update user preferences only
     * PUT /api/patients/{userId}/profile/preferences
     */
//    @PutMapping("/{userId}/profile/preferences")
//    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
//    public ResponseEntity<ApiResponseDto> updatePreferences(
//            @PathVariable @NotNull Long userId,
//            @CurrentUser UserPrincipal currentUser,
//            @Valid @RequestBody PreferencesUpdateDto preferences) {
//        try {
//            validateUserAccess(userId, currentUser);
//
//            PatientProfileResponseDto updatedProfile = patientProfileService.updatePreferences(
//                    userId, preferences);
//
//            return ResponseEntity.ok(
//                    new ApiResponseDto(true, "Preferences updated successfully", updatedProfile)
//            );
//        } catch (UnauthorizedException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(new ApiResponseDto(false, "Access denied"));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponseDto(false, "Failed to update preferences: " + e.getMessage()));
//        }
//    }

    // =============================================
    // FILE MANAGEMENT ENDPOINTS
    // =============================================

    /**
     * Upload profile picture
     * POST /api/patients/{userId}/profile/picture
     */
//    @PostMapping(value = "/{userId}/profile/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
//    public ResponseEntity<ApiResponseDto> uploadProfilePicture(
//            @PathVariable @NotNull Long userId,
//            @CurrentUser UserPrincipal currentUser,
//            @RequestParam("profilePicture") MultipartFile file) {
//        try {
//            validateUserAccess(userId, currentUser);
//
//            if (file.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(new ApiResponseDto(false, "Please select a file to upload"));
//            }
//
//            FileUploadResponseDto response = patientProfileService.uploadProfilePicture(userId, file);
//
//            return ResponseEntity.ok(
//                    new ApiResponseDto(true, "Profile picture uploaded successfully", response)
//            );
//        } catch (UnauthorizedException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(new ApiResponseDto(false, "Access denied"));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponseDto(false, e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponseDto(false, "Failed to upload profile picture: " + e.getMessage()));
//        }
//    }

    /**
     * Delete profile picture
     * DELETE /api/patients/{userId}/profile/picture
     */
//    @DeleteMapping("/{userId}/profile/picture")
//    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
//    public ResponseEntity<ApiResponseDto> deleteProfilePicture(
//            @PathVariable @NotNull Long userId,
//            @CurrentUser UserPrincipal currentUser) {
//        try {
//            validateUserAccess(userId, currentUser);
//
//            patientProfileService.deleteProfilePicture(userId);
//
//            return ResponseEntity.ok(
//                    new ApiResponseDto(true, "Profile picture deleted successfully")
//            );
//        } catch (UnauthorizedException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(new ApiResponseDto(false, "Access denied"));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponseDto(false, "Profile picture not found"));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponseDto(false, "Failed to delete profile picture: " + e.getMessage()));
//        }
//    }

    /**
     * Get profile picture URL
     * GET /api/patients/{userId}/profile/picture
     */
//    @GetMapping("/{userId}/profile/picture")
//    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR') or hasRole('ADMIN')")
//    public ResponseEntity<ApiResponseDto> getProfilePictureUrl(
//            @PathVariable @NotNull Long userId,
//            @CurrentUser UserPrincipal currentUser) {
//        try {
//            // More lenient access for profile pictures (doctors can view patient pictures)
//            if (!hasProfilePictureAccess(userId, currentUser)) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body(new ApiResponseDto(false, "Access denied"));
//            }
//
//            String profilePictureUrl = patientProfileService.getProfilePictureUrl(userId);
//
//            return ResponseEntity.ok(
//                    new ApiResponseDto(true, "Profile picture URL retrieved", profilePictureUrl)
//            );
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponseDto(false, "Profile picture not found"));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponseDto(false, "Failed to retrieve profile picture"));
//        }
//    }

    // =============================================
    // VALIDATION ENDPOINTS
    // =============================================

    /**
     * Validate South African ID Number
     * POST /api/patients/{userId}/profile/validate-id
     */
    @PostMapping("/{userId}/profile/validate-id")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> validateIdNumber(
            @PathVariable @NotNull Long userId,
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody IdValidationRequestDto request) {
        try {
            validateUserAccess(userId, currentUser);

            IdValidationResponseDto response = patientProfileService.validateSouthAfricanId(
                    request.getIdNumber());

            return ResponseEntity.ok(
                    new ApiResponseDto(true, "ID validation completed", response)
            );
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto(false, "ID validation failed: " + e.getMessage()));
        }
    }

    /**
     * Check if ID number is available (not already used)
     * POST /api/patients/{userId}/profile/check-id-availability
     */
    @PostMapping("/{userId}/profile/check-id-availability")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> checkIdAvailability(
            @PathVariable @NotNull Long userId,
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody IdValidationRequestDto request) {
        try {
            validateUserAccess(userId, currentUser);

            boolean isAvailable = patientProfileService.isIdNumberAvailable(
                    request.getIdNumber(), userId);

            String message = isAvailable ?
                    "ID number is available" :
                    "ID number is already registered";

            return ResponseEntity.ok(
                    new ApiResponseDto(isAvailable, message, isAvailable)
            );
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(false, "Failed to check ID availability"));
        }
    }

    // =============================================
    // PROFILE EXPORT ENDPOINTS
    // =============================================

    /**
     * Export patient profile as PDF
     * GET /api/patients/{userId}/profile/export/pdf
     */
//    @GetMapping("/{userId}/profile/export/pdf")
//    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
//    public ResponseEntity<?> exportProfileAsPdf(
//            @PathVariable @NotNull Long userId,
//            @CurrentUser UserPrincipal currentUser) {
//        try {
//            validateUserAccess(userId, currentUser);
//
//            byte[] pdfData = patientProfileService.generateProfilePdf(userId);
//
//            return ResponseEntity.ok()
//                    .header("Content-Type", "application/pdf")
//                    .header("Content-Disposition", "attachment; filename=\"patient_profile_" + userId + ".pdf\"")
//                    .body(pdfData);
//        } catch (UnauthorizedException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(new ApiResponseDto(false, "Access denied"));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponseDto(false, "Failed to export profile"));
//        }
//    }

    /**
     * Export patient profile as JSON
     * GET /api/patients/{userId}/profile/export/json
     */
    @GetMapping("/{userId}/profile/export/json")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> exportProfileAsJson(
            @PathVariable @NotNull Long userId,
            @CurrentUser UserPrincipal currentUser) {
        try {
            validateUserAccess(userId, currentUser);

            PatientProfileResponseDto profile = patientProfileService.getPatientProfile(userId);

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .header("Content-Disposition", "attachment; filename=\"patient_profile_" + userId + ".json\"")
                    .body(new ApiResponseDto(true, "Profile exported successfully", profile));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDto(false, "Access denied"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(false, "Failed to export profile"));
        }
    }

    // =============================================
    // PROFILE STATISTICS ENDPOINTS
    // =============================================

    /**
     * Get patient profile statistics
     * GET /api/patients/{userId}/profile/stats
     */
//    @GetMapping("/{userId}/profile/stats")
//    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
//    public ResponseEntity<ApiResponseDto> getProfileStatistics(
//            @PathVariable @NotNull Long userId,
//            @CurrentUser UserPrincipal currentUser) {
//        try {
//            validateUserAccess(userId, currentUser);
//
//            ProfileStatisticsDto stats = patientProfileService.getProfileStatistics(userId);
//
//            return ResponseEntity.ok(
//                    new ApiResponseDto(true, "Profile statistics retrieved", stats)
//            );
//        } catch (UnauthorizedException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(new ApiResponseDto(false, "Access denied"));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponseDto(false, "Failed to retrieve statistics"));
//        }
//    }

    // =============================================
    // HELPER METHODS
    // =============================================

    /**
     * Validate that current user has access to modify the specified userId's profile
     */
    private void validateUserAccess(Long userId, UserPrincipal currentUser) {
        if (currentUser == null) {
            throw new UnauthorizedException("Authentication required");
        }

        // Admin can access any profile
        if (currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
            return;
        }

        // Users can only access their own profile
        if (!userId.equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only access your own profile");
        }
    }

    /**
     * Check if current user has access to view profile pictures
     * More lenient than profile modification access
     */
    private boolean hasProfilePictureAccess(Long userId, UserPrincipal currentUser) {
        if (currentUser == null) return false;

        // Admin can view any profile picture
        if (currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        // Users can view their own profile picture
        if (userId.equals(currentUser.getId())) {
            return true;
        }

        // Doctors can view patient profile pictures (if they have a relationship)
        if (currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_DOCTOR"))) {
            // TODO: Add logic to check if doctor has relationship with patient
            return patientProfileService.hasDoctorPatientRelationship(currentUser.getId(), userId);
        }

        return false;
    }
}
