package com.ayanda.HealthEaseApi.dto.dtoObjects;

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
    private String medicalAidNumber;
    private String dependentCode;
    private Boolean authorizationRequired = false;
}
