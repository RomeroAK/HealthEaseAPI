package com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
