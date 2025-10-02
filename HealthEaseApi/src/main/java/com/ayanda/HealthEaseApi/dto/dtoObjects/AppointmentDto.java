package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.ayanda.HealthEaseApi.entities.Appointment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {

    private Long id;

    private PatientSummaryDto patient;
    private DoctorSummaryDto doctor;

    private LocalDate appointmentDate;

    private String appointmentType;

    private Appointment.AppointmentStatus status;

    private String reason;
}
