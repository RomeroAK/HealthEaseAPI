package com.ayanda.HealthEaseApi.dto.dtoObjects;


import com.ayanda.HealthEaseApi.entities.Doctor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAvailabilityDto {

    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private int slotDurationMinutes;
    private Doctor doctor;
    private String dayOfWeek;
    private boolean isActive;

}
