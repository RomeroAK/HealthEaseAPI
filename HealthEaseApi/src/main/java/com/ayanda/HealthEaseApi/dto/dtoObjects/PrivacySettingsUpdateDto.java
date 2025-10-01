package com.ayanda.HealthEaseApi.dto.dtoObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivacySettingsUpdateDto {
    private Boolean shareDataWithResearch = false;
    private Boolean allowMarketingCommunication = false;
    private String profileVisibility = "private";
}
