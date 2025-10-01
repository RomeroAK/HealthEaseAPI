package com.ayanda.HealthEaseApi.dto.dtoObjects;


import com.ayanda.HealthEaseApi.entities.Doctor;
import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class DoctorAvailability {

    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private int slotDurationMinutes;
    private Doctor doctor;
    private String dayOfWeek;
    private boolean isActive;

}
