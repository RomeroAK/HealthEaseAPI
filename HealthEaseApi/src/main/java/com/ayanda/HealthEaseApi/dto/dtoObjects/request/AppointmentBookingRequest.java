package com.ayanda.HealthEaseApi.dto.dtoObjects.request;


import com.ayanda.HealthEaseApi.entities.Appointment;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentBookingRequest {

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDate appointmentDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    private Appointment.AppointmentStatus status;

    @NotNull(message = "Appointment type is required")
    private Appointment.AppointmentType type;

    @NotBlank(message = "Reason is required")
    @Size(min = 10, max = 500)
    private String reason;

    private List<String> symptoms;

    @Size(max = 1000)
    private String patientNotes;

    private Appointment.ConsultationMode consultationMode;
    private String preferredLocation;

    private String doctorName;


    private Integer duraction;
}
