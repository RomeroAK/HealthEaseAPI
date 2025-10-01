package com.ayanda.HealthEaseApi.dto.dtoObjects;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllergyUpdateDto {
    @NotBlank(message = "Allergy name is required")
    private String name;
    private String severity;
    private String description;
    private String notes;
}
