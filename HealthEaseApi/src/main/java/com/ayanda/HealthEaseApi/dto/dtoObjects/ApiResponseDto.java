package com.ayanda.HealthEaseApi.dto.dtoObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto {
    private boolean success;
    private String message;
    private Object data;

    public ApiResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
