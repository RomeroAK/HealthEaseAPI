package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceDto {
    private Long id;
    private String provider;
    private String policyNumber;
    private String groupNumber;
    private String planName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
    private BigDecimal copayAmount;
    private BigDecimal deductibleAmount;
    private String secondaryProvider;
    private String secondaryPolicyNumber;
    private String secondaryGroupNumber;
    private String secondaryPlanName;
    private String dependentCode;
    private Boolean authorizationRequired;
}
