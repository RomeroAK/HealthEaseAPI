package com.ayanda.HealthEaseApi.additional;

public enum UserRole {
    PATIENT("P"),
    DOCTOR("D"),
    ADMIN("A");

    private String code;

    UserRole(String code) {
        this.code = code;
    }
}
