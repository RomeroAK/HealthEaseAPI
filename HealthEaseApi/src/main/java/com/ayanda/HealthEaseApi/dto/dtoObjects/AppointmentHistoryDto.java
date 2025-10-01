package com.ayanda.HealthEaseApi.dto.dtoObjects;


import com.ayanda.HealthEaseApi.entities.Appointment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentHistoryDto {

    private Long id;

    private Long appointmentId;

    private String action; // CREATED, CONFIRMED, CANCELLED, RESCHEDULED, CHECKED_IN, STARTED, COMPLETED

    private Appointment.AppointmentStatus oldStatus;

    private Appointment.AppointmentStatus newStatus;

    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private String updatedBy; // user who performed the action

    private String updatedByName; // display name of the user

    private String actionDescription; // human-readable description of the action

    // Additional details for specific actions
    private String previousValues; // JSON string of previous values for updates

    private String newValues; // JSON string of new values for updates

    // Helper method to get a user-friendly action description
    public String getFormattedAction() {
        if (actionDescription != null && !actionDescription.isEmpty()) {
            return actionDescription;
        }

        return switch (action) {
            case "CREATED" -> "Appointment was created";
            case "CONFIRMED" -> "Appointment was confirmed";
            case "CANCELLED" -> "Appointment was cancelled";
            case "RESCHEDULED" -> "Appointment was rescheduled";
            case "CHECKED_IN" -> "Patient checked in";
            case "STARTED" -> "Consultation started";
            case "COMPLETED" -> "Consultation completed";
            default -> action;
        };
    }

    // Helper method to get status change description
    public String getStatusChangeDescription() {
        if (oldStatus == null) {
            return "Status set to " + newStatus;
        }
        if (oldStatus.equals(newStatus)) {
            return "Status remains " + newStatus;
        }
        return "Status changed from " + oldStatus + " to " + newStatus;
    }
}
