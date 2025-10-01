package com.ayanda.HealthEaseApi.additional;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationFees {

    @DecimalMin(value = "0.0", message = "Initial consultation fee must be positive")
    @Column(name = "initial_consultation", precision = 8, scale = 2)
    private BigDecimal initialConsultation;

    @DecimalMin(value = "0.0", message = "Follow-up consultation fee must be positive")
    @Column(name = "follow_up_consultation", precision = 8, scale = 2)
    private BigDecimal followUpConsultation;

    @DecimalMin(value = "0.0", message = "Virtual consultation fee must be positive")
    @Column(name = "virtual_consultation", precision = 8, scale = 2)
    private BigDecimal virtualConsultation;

    @DecimalMin(value = "0.0", message = "Emergency consultation fee must be positive")
    @Column(name = "emergency_consultation", precision = 8, scale = 2)
    private BigDecimal emergencyConsultation;
}
