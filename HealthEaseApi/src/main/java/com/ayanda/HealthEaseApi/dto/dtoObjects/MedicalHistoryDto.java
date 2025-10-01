package com.ayanda.HealthEaseApi.dto.dtoObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistoryDto {
    private String bloodType;
    private Integer height;
    private Integer weight;
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
}
