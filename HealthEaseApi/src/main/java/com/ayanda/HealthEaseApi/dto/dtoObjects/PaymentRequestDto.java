package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Builder.Default
    private String currency = "ZAR"; // South African Rand

    @NotNull(message = "Appointment ID is required")
    @JsonProperty("appointment_id")
    private Long appointmentId;

    private String paymentMethod; // Optional: "Credit Card", "EFT", "Instant EFT"

    @Builder.Default
    private String gateway = "PayFast";

    // Patient details (optional, can be fetched from appointment)
    private String patientEmail;
    private String patientFirstName;
    private String patientLastName;
    private String patientPhone;

    // Return URLs (optional, will use defaults if not provided)
    private String returnUrl;
    private String cancelUrl;
    private String notifyUrl;

    // Additional metadata
    private String description;
    private String itemName;

    // Validation helper
    public boolean isValid() {
        return amount != null &&
                amount.compareTo(BigDecimal.ZERO) > 0 &&
                appointmentId != null;
    }
}
