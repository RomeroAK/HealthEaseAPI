package com.ayanda.HealthEaseApi.dto.dtoObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationStatusUpdateDto {
    private Boolean covid19;
    private Boolean flu;
    private Boolean hepatitisBs;
    private Boolean tetanus;
    private String other;
}
