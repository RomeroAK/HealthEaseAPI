package com.ayanda.HealthEaseApi.service;

import com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot.OpenAIRequest;
import com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot.OpenAIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIService {

    @Autowired
    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OpenAIResponse getResponse(OpenAIRequest requestBody) {
        String apiKey = "";
        //subsitutue with actual key management solution later
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OpenAIRequest> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<OpenAIResponse> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                entity,
                OpenAIResponse.class
        );

        return response.getBody();
    }
}
