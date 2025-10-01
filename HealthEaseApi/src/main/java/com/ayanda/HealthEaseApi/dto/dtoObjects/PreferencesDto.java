package com.ayanda.HealthEaseApi.dto.dtoObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreferencesDto {
    private String preferredDoctorGender;
    private String preferredLanguage;
    private CommunicationPreferencesDto communicationPreferences;
    private PrivacySettingsDto privacySettings;
}
