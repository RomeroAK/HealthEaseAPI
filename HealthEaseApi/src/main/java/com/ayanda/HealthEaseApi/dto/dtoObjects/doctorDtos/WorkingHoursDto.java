package com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkingHoursDto {

    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime lunchBreakStart;
    private LocalTime lunchBreakEnd;
    private Boolean isAvailable;
}
