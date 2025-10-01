package com.ayanda.HealthEaseApi.dto.dtoObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationPreferencesDto {
    private Boolean emailReminders;
    private Boolean smsReminders;
    private Boolean appointmentConfirmations;
    private Boolean testResults;
    private Boolean promotionalEmails;
}
