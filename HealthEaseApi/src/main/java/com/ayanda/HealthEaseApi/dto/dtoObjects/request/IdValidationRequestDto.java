package com.ayanda.HealthEaseApi.dto.dtoObjects.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdValidationRequestDto {
    @NotBlank(message = "ID number is required")
    @Pattern(regexp = "^(\\d{13}|[A-Z]{2}\\d{6}[A-Z]\\d{2})$", message = "ID number must be either a 13-digit South African ID ")
    private String idNumber;
}
