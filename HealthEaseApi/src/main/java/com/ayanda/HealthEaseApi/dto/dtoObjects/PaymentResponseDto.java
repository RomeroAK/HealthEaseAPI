package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponseDto {

    private boolean success;

    private String message;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("reference_number")
    private String referenceNumber;

    @JsonProperty("payment_url")
    private String paymentUrl; // PayFast payment URL

    private BigDecimal amount;

    private String currency;

    private String status;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("gateway_response")
    private String gatewayResponse;

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("completed_at")
    private LocalDateTime completedAt;

    // Additional payment details
    @JsonProperty("appointment_id")
    private Long appointmentId;

    @JsonProperty("requires_action")
    private Boolean requiresAction; // If user needs to complete payment at URL

    // Helper methods
    public static PaymentResponseDto success(String transactionId, String message) {
        return PaymentResponseDto.builder()
                .success(true)
                .transactionId(transactionId)
                .message(message)
                .build();
    }

    public static PaymentResponseDto failure(String errorMessage) {
        return PaymentResponseDto.builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }

    public static PaymentResponseDto pending(String paymentUrl, String referenceNumber) {
        return PaymentResponseDto.builder()
                .success(true)
                .paymentUrl(paymentUrl)
                .referenceNumber(referenceNumber)
                .requiresAction(true)
                .message("Please complete payment at the provided URL")
                .build();
    }
}
