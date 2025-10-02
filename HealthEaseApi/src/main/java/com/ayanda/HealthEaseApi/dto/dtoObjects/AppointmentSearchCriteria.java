package com.ayanda.HealthEaseApi.dto.dtoObjects;


import com.ayanda.HealthEaseApi.entities.Appointment;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class AppointmentSearchCriteria {

    private Long doctorId;
    private Long patientId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private List<Appointment.AppointmentStatus> statuses;

    private String reasonForVisit;

    private String patientName;

    private String doctorName;

    private String confirmationCode;

    private Boolean followUpRequired;

    private Boolean emergencyAppointment;

    // Sort options
    @Builder.Default
    private String sortBy = "appointmentDate"; // appointmentDate, createdAt, fee, duration
    @Builder.Default
    private String sortDirection = "DESC"; // ASC, DESC
}
