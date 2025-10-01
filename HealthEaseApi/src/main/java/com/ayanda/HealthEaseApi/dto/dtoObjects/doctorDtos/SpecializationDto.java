package com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecializationDto {
    private Long id;
    private String name;
    private String category;
    private String description;
}
