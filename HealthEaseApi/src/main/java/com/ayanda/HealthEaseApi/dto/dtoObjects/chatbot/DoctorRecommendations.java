package com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRecommendations {
    private Long id;
    private String name;
    private String specialty;
    private Double rating;
    private Integer experienceYears;
    private String location;
    private Boolean availableToday;
    private BigDecimal consultationFee;
}
