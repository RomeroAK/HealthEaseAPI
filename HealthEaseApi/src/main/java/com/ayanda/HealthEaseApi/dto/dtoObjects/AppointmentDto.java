package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.ayanda.HealthEaseApi.entities.Appointment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {

    private Long id;
    private Long patientId;
    private Long doctorId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime appointmentDateTime;
    private String appointmentType;
//    private Appointment.AppointmentStatus status;
    private String status;
    private String doctorName;
    private BigDecimal consultationFee;
    private String reason;
}
