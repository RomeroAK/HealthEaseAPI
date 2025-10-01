package com.ayanda.HealthEaseApi.dto.dtoObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdValidationResponseDto {
    private boolean valid;
    private String message;
    private IdExtractedInfoDto extractedInfo;
}
