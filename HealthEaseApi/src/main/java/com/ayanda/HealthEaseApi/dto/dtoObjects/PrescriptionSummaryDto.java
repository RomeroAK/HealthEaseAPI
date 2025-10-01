package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionSummaryDto {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String medicationName;
    private String dosage;
    private LocalDateTime prescribedAt;
    private String frequency;
    private String notes;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issuedAt;
    private boolean isActive;
}
