package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurgeryDto {
    private Long id;
    private String name;
    private String surgeryName;
    private String description;
    private String notes;
    private Long patientId;
    private Long doctorId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
}
