package com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificationDto {

    private Long id;

    @NotBlank(message = "Certification name is required")
    private String name;

    @NotBlank(message = "Issuing organization is required")
    private String issuingOrganization;

    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String credentialId;
    private String verificationUrl;
}
