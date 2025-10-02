package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.ayanda.HealthEaseApi.entities.Doctor;
import com.ayanda.HealthEaseApi.entities.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSummaryDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String email;
    private String phoneNumber;
    private String practiceName;
    private String primarySpecialization;

    public static DoctorSummaryDto fromEntity(Doctor doctor) {
        return DoctorSummaryDto.builder()
                .id(doctor.getDoctorId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .licenseNumber(doctor.getMedicalLicenseNumber())
                .phoneNumber(doctor.getPhoneNumber())
                .practiceName(doctor.getPracticeName())
                .primarySpecialization(doctor.getSpecialization())
                .build();
    }
}
