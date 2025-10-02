package com.ayanda.HealthEaseApi.dto.dtoObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalHistoryDto {
    private Long id;
    private String bloodType;
    private Integer height;
    private Integer weight;
    private LocalDateTime created;
    private List<AllergyDto> allergies;
    private List<ConditionDto> chronicConditions;
    private List<MedicationDto> currentMedications;
    private List<SurgeryDto> previousSurgeries;
    private String familyMedicalHistory;
    private String smokingStatus;
    private String alcoholConsumption;
    private String exerciseFrequency;
    private String dietaryRestrictions;
    private String vaccinationStatus;
    private PatientSummaryDto patient;
    private DoctorSummaryDto doctor;
}
