package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PaymentDto {

    private Long id;

    @JsonProperty("reference_number")
    private String referenceNumber;

    @JsonProperty("transaction_id")
    private String transactionId;

    private BigDecimal amount;

    private String currency;

    @JsonProperty("payment_method")
    private String paymentMethod;

    private String gateway;

    private String status;

    @JsonProperty("appointment_id")
    private Long appointmentId;

    @JsonProperty("is_refund")
    private boolean isRefund;

    @JsonProperty("original_payment_id")
    private Long originalPaymentId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("completed_at")
    private LocalDateTime completedAt;

    @JsonProperty("failure_reason")
    private String failureReason;
}
