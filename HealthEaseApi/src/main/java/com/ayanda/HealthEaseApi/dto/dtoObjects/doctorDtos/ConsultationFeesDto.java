package com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationFeesDto {

    @DecimalMin(value = "0.0", message = "Initial consultation fee must be positive")
    private BigDecimal initialConsultation;

    @DecimalMin(value = "0.0", message = "Follow-up consultation fee must be positive")
    private BigDecimal followUpConsultation;

    @DecimalMin(value = "0.0", message = "Virtual consultation fee must be positive")
    private BigDecimal virtualConsultation;

    @DecimalMin(value = "0.0", message = "Emergency consultation fee must be positive")
    private BigDecimal emergencyConsultation;
}
