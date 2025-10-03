package com.ayanda.HealthEaseApi.entities;


import com.ayanda.HealthEaseApi.additional.ConsultationFees;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table(name = "doctors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @Column(name = "main_user_id", unique = true, nullable = false)
    private Long userId;

    // Basic Information
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Size(max = 100, message = "Full name must be less than 100 characters")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Pattern(regexp = "^[0-9]{13}$", message = "ID number must be 13 digits")
    @Column(name = "id_number", unique = true)
    private String idNumber;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    // Professional Information
    @NotBlank(message = "Medical license number is required")
    @Column(name = "medical_license_number", unique = true, nullable = false)
    private String medicalLicenseNumber;

    @Min(value = 0, message = "Years of experience cannot be negative")
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;


    @NotBlank(message = "Specialization number is required")
    @Column(name = "specialization", nullable = false)
    private String specialization;

    // Practice Information
    @NotBlank(message = "Practice name is required")
    @Column(name = "practice_name", nullable = false)
    private String practiceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "practice_type")
    private PracticeType practiceType;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "doctor_consultation_types", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "consultation_type")
    @Builder.Default
    private List<ConsultationType> consultationTypes = new ArrayList<>();

    @Embedded
    private ConsultationFees consultationFees;

    @Builder.Default
    @Column(name = "consultation_fee", precision = 10, scale = 2)
    private BigDecimal consultationFee = BigDecimal.ZERO;;

    @Column(length = 1000)
    private String bio;

    @Column(name = "emergency_available")
    @Builder.Default
    private Boolean emergencyAvailable = false;

    @Column(name = "accepts_insurance")
    @Builder.Default
    private Boolean acceptsInsurance = false;

    @ElementCollection
    @CollectionTable(name = "doctor_insurance_providers", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "insurance_provider")
    @Builder.Default
    private List<String> acceptedInsuranceProviders = new ArrayList<>();

    // System fields
    @Column(name = "is_verified")
    @Builder.Default
    private Boolean isVerified = false;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "review_count")
    @Builder.Default
    private Integer reviewCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_address_id", referencedColumnName = "addressId")
    private Address clinicAddress;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_address_id", referencedColumnName = "addressId")
    private Address hospitalAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "latitude")
    private Double Latitude;

    @Column(name = "longitude")
    private Double longitude;

    @ManyToMany
    @Builder.Default
    private List<Patient> patients = new ArrayList<>();

    // Enums
    public enum Gender {
        MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY
    }

    public enum PracticeType {
        PRIVATE, PUBLIC, CLINIC, GROUP, ACADEMIC
    }

    public enum ConsultationType {
        IN_PERSON, VIRTUAL, BOTH
    }

}
