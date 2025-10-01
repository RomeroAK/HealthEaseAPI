package com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatBotResponseDto {
    private String message;
    private List<String> suggestions;
    private List<DoctorRecommendations> doctorRecommendations;
    private List<String> homeRemedies;
    private Map<String, Object> metadata;
    private Boolean requiresHumanIntervention;
}
