package com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper;

import java.time.LocalDate;
import java.time.Period;

public class HelperClass {

    public static int calculateAgeFromID(String idNumber) {
        String birthDate = idNumber.substring(0, 6); // YYMMDD
        int year = Integer.parseInt(birthDate.substring(0, 2));
        int month = Integer.parseInt(birthDate.substring(2, 4));
        int day = Integer.parseInt(birthDate.substring(4, 6));

        // Assume 1900s if above current year, else 2000s
        int currentYear = LocalDate.now().getYear() % 100;
        year += (year > currentYear) ? 1900 : 2000;

        LocalDate birth = LocalDate.of(year, month, day);
        return Period.between(birth, LocalDate.now()).getYears();
    }

    public static String extractGenderFromID(String idNumber) {
        int genderDigits = Integer.parseInt(idNumber.substring(6, 10));
        return (genderDigits >= 5000) ? "MALE" : "FEMALE";
    }

    public static String validateString(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        return str;
    }
}

