package com.ayanda.HealthEaseApi.dto.dtoObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCompletionDto {
    private int completionPercentage;
    private int completedFields;
    private int totalFields;
    private List<String> missingFields;
    private boolean isComplete;
    private boolean personalInfoComplete;
    private boolean medicalInfoComplete;
    private boolean emergencyContactsComplete;
    private boolean insuranceInfoComplete;
}
