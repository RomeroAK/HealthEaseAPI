package com.ayanda.HealthEaseApi.additional;

public enum MedicalField {
    GENERAL_PRACTITIONER("GP"),
    CARDIOLOGIST("CARD"),
    DERMATOLOGIST("DERM"),
    NEUROLOGIST("NEUR"),
    ORTHOPEDIC_SURGEON("ORTH"),
    PEDIATRICIAN("PEDIA"),
    PSYCHIATRIST("PSYCH"),
    ONCOLOGIST("ONCO"),
    DENTIST("DENT"),
    GYNECOLOGIST("GYNE");

    // You can add methods or properties if needed
    private String code;

    MedicalField(String code) {
        this.code = code;
    }
}
