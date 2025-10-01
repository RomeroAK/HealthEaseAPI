package com.ayanda.HealthEaseApi.dto.dtoObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreferencesUpdateDto {
    private String preferredDoctorGender;
    private String preferredLanguage;
    private CommunicationPreferencesUpdateDto communicationPreferences;
    private PrivacySettingsUpdateDto privacySettings;
}
