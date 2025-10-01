package com.ayanda.HealthEaseApi.dto.dtoObjects.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRescheduleRequest {

    @NotNull(message = "New appointment date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate newDate;

    @NotNull(message = "New start time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime newStartTime;

    @NotNull(message = "New end time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime newEndTime;

    @Positive(message = "Duration must be positive")
    private Integer newDuration; // in minutes

    private String reason; // reason for rescheduling

    private String notes; // additional notes

    // Validation method to ensure end time is after start time
    public boolean isValidTimeRange() {
        if (newStartTime == null || newEndTime == null) {
            return false;
        }
        return newEndTime.isAfter(newStartTime);
    }

    // Calculate duration from start and end times
    public Integer calculateDuration() {
        if (newStartTime == null || newEndTime == null) {
            return null;
        }
        return (int) java.time.Duration.between(newStartTime, newEndTime).toMinutes();
    }
}
