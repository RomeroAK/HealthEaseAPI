package com.ayanda.HealthEaseApi.dto.dtoObjects;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalInfoUpdateDto {
    private String bloodType;

    @Min(value = 50, message = "Height must be at least 50cm")
    @Max(value = 300, message = "Height must not exceed 300cm")
    private Integer height;

    @Min(value = 20, message = "Weight must be at least 20kg")
    @Max(value = 500, message = "Weight must not exceed 500kg")
    private Integer weight;

    private List<AllergyUpdateDto> allergies;
    private List<ConditionUpdateDto> chronicConditions;
    private List<MedicationUpdateDto> currentMedications;
    private List<SurgeryUpdateDto> previousSurgeries;
    private String familyMedicalHistory;
    private String smokingStatus;
    private String alcoholConsumption;
    private String exerciseFrequency;
    private String dietaryRestrictions;
    private VaccinationStatusUpdateDto vaccinationStatus;
}
