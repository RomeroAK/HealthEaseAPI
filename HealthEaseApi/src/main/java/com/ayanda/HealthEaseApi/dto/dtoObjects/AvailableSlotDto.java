package com.ayanda.HealthEaseApi.dto.dtoObjects;



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
public class AvailableSlotDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private Integer durationMinutes;
    private Boolean isAvailable;
    private String unavailableReason;
    private String DoctorName;
    private Long DoctorId;
}
