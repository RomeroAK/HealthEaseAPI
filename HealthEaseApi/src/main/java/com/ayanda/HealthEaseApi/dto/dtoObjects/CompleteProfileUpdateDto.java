package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.DoctorInfoUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteProfileUpdateDto {
    private PersonalInfoUpdateDto personalInfo;
    private MedicalInfoUpdateDto medicalHistory;
    private List<EmergencyContactUpdateDto> emergencyContacts;
    private InsuranceInfoUpdateDto insurance;
    private PreferencesUpdateDto preferences;
    private DoctorInfoUpdateDto doctorInfo;
}
