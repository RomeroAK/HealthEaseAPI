package com.ayanda.HealthEaseApi.dto.dtoObjects;

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
public class PatientProfileResponseDto {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String alternatePhoneNumber;
    private String idNumber;
    private String occupation;
    private String employer;
    private String maritalStatus;
    private String preferredLanguage;
    private String profilePicture;
    private AddressDto address;
    private MedicalHistoryDto medicalHistory;
    private List<EmergencyContactDto> emergencyContacts;
    private InsuranceDto insurance;
    private String medicalAidNumber;
    private PreferencesDto preferences;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
