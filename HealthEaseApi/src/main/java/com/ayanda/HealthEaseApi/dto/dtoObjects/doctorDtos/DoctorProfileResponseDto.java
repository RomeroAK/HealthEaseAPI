package com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos;


import com.ayanda.HealthEaseApi.dto.dtoObjects.AddressDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorProfileResponseDto {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String specialization;
    private String licenseNumber;
    private int yearsOfExperience;
    private List<String> qualifications;
    private String medicalSchool;
    private String practiceType;
    private AddressDto clinicAddress;
    private AddressDto hospitalAddress;
  private boolean isActive;
  private boolean isPrivatePractice;
  private List<String> acceptedInsurance;
  private String practiceName;
    private String bio;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
