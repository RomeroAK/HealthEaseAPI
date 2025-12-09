package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceInfoUpdateDto {
    private Boolean hasInsurance = false;
    private PrimaryInsuranceUpdateDto primaryInsurance;
    private SecondaryInsuranceUpdateDto secondaryInsurance;

    @JsonProperty("medicalAidNumber")
    private String medicalAidNumber;
    private String dependentCode;
    private Boolean authorizationRequired = false;
}
