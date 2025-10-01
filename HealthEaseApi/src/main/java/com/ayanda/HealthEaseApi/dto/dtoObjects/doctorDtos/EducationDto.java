package com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationDto {

    private Long id;

    @NotBlank(message = "Institution is required")
    private String institution;

    @NotBlank(message = "Degree is required")
    private String degree;

    @NotBlank(message = "Field of study is required")
    private String fieldOfStudy;

    @Min(value = 1950, message = "Start year must be after 1950")
    private Integer startYear;

    @Min(value = 1950, message = "End year must be after 1950")
    private Integer endYear;

    private Boolean isCurrentlyStudying;
    private Double gpa;
    private String honors;
}
