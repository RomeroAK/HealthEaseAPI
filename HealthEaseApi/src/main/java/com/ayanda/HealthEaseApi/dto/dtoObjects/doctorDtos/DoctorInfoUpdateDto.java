package com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos;

import com.ayanda.HealthEaseApi.dto.dtoObjects.AddressUpdateDto;
import com.ayanda.HealthEaseApi.entities.Address;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorInfoUpdateDto {
    private Long id;
    private Long userId;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(\\+27|0)[6-8][0-9]{8}$", message = "Invalid South African phone number")
    private String telephone;
    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "specialization is required")
    private String specialization;
    @NotBlank(message = "License Number is required")
    private String licenseNumber;

    private BigDecimal consultationFee;

    private List<String> acceptedInsurance;
    private boolean isPrivatePractice;

    private String practiceName;
    @NotBlank(message = "Hospital Name is required")
    private String hospitalName;

    private boolean isActive;

    @NotNull(message = "Address is required")
    private AddressUpdateDto clinicAddress;
    @NotNull(message = "Address is required")
    private AddressUpdateDto hospitalAddress;

    private String bio;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
