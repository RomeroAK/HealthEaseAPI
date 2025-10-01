package com.ayanda.HealthEaseApi.dto.dtoObjects;


import com.ayanda.HealthEaseApi.entities.Doctor;
import com.ayanda.HealthEaseApi.entities.Patient;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurgeryUpdateDto {
    @NotBlank(message = "Surgery name is required")
    private String name;
    private String description;
    private String notes;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    private Doctor doctor;
    private Patient user;

}
