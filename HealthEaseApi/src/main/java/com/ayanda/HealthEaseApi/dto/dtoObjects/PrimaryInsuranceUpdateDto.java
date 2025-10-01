package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrimaryInsuranceUpdateDto {
    @NotBlank(message = "Insurance provider is required")
    private String provider;

    @NotBlank(message = "Policy number is required")
    private String policyNumber;

    private String groupNumber;
    private String planName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    @DecimalMin(value = "0.0", message = "Copay amount must be positive")
    private BigDecimal copayAmount;

    @DecimalMin(value = "0.0", message = "Deductible amount must be positive")
    private BigDecimal deductibleAmount;
}
