package com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos;


import com.ayanda.HealthEaseApi.dto.dtoObjects.AddressDto;
import com.ayanda.HealthEaseApi.entities.Doctor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorProfileDto {

    private Long id;
    private Long userId;

    // Basic Information
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    private LocalDate dateOfBirth;
    private Doctor.Gender gender;

    @Pattern(regexp = "^[0-9]{13}$", message = "ID number must be 13 digits")
    private String idNumber;

    private String profilePictureUrl;

    // Professional Information
    @NotBlank(message = "Medical license number is required")
    private String medicalLicenseNumber;

    @NotBlank(message = "HPCSA number is required")
    private String hpcsaNumber;

    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;

    private List<Long> specializationIds;
    private List<String> subspecialties;
    private List<String> languagesSpoken;

    // Education & Qualifications
    @Valid
    private List<EducationDto> education;

    @Valid
    private List<CertificationDto> certifications;

    // Practice Information
    @NotBlank(message = "Practice name is required")
    private String practiceName;

    private Doctor.PracticeType practiceType;

    @Valid
    private AddressDto address;

    private List<Doctor.ConsultationType> consultationTypes;

    @Valid
    private ConsultationFeesDto consultationFees;

    // Availability
    @Valid
    private List<WorkingHoursDto> workingHours;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    private Boolean emergencyAvailable;
    private Boolean acceptsInsurance;
    private List<String> acceptedInsuranceProviders;

    // Terms acceptance
    @AssertTrue(message = "You must agree to the terms and conditions")
    private Boolean agreeToTerms;

    @AssertTrue(message = "You must agree to the privacy policy")
    private Boolean agreeToPrivacyPolicy;

    // System fields (read-only)
    private Boolean isVerified;
    private Boolean isActive;
    private BigDecimal rating;
    private Integer reviewCount;
}
