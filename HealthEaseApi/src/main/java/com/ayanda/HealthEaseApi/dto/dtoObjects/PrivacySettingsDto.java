package com.ayanda.HealthEaseApi.dto.dtoObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivacySettingsDto {
    private Boolean shareDataWithResearch;
    private Boolean allowMarketingCommunication;
    private String profileVisibility;
}
