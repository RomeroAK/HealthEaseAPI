package com.ayanda.HealthEaseApi.dto.dtoObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationPreferencesUpdateDto {
    private Boolean emailReminders = true;
    private Boolean smsReminders = true;
    private Boolean appointmentConfirmations = true;
    private Boolean testResults = true;
    private Boolean promotionalEmails = false;
}
