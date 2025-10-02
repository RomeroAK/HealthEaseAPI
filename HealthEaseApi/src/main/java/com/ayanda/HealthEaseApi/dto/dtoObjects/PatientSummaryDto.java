package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.ayanda.HealthEaseApi.entities.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientSummaryDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public static PatientSummaryDto fromEntity(Patient patient) {
        return PatientSummaryDto.builder()
                .id(patient.getPatientId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .phoneNumber(patient.getPhoneNumber())
                .build();
    }
}
