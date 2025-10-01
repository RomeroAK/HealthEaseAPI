package com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
