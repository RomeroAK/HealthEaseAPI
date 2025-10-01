package com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos;


import com.ayanda.HealthEaseApi.entities.Doctor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorSearchCriteria {

    // Name search
    private String name;

    // Practice search
    private String practiceName;

    // Specialization search
    private String specialization;

    // Location-based search
    private Double latitude;
    private Double longitude;
    private Double radius; // in kilometers
    private String province;
    private String city;

    // Consultation preferences
    private Doctor.ConsultationType consultationType;
    private BigDecimal maxFee;
    private BigDecimal minFee;

    // Rating and reviews
    private BigDecimal minRating;
    private Integer minReviewCount;

    // Availability
    private Boolean emergencyAvailable;

    // Insurance
    private Boolean acceptsInsurance;
    private String insuranceProvider;

    // Languages
    private List<String> languagesSpoken;

    // System filters
    private Boolean isActive;
    private Boolean isVerified;

    // Experience
    private Integer minYearsOfExperience;
    private Integer maxYearsOfExperience;

    // Gender preference
    private Doctor.Gender gender;

    // Practice type
    private Doctor.PracticeType practiceType;
}
