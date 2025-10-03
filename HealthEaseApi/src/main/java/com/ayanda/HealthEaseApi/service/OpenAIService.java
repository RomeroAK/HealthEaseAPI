package com.ayanda.HealthEaseApi.service;

import com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot.OpenAIRequest;
import com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot.OpenAIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public OpenAIService(WebClient openAIWebClient) {
        this.webClient = openAIWebClient;
    }

    public Mono<String> getResponse(String prompt) {
        OpenAIRequest request = new OpenAIRequest();
        request.setModel("gpt-4");
        request.setMax_tokens(100);
        request.setMessages(List.of(new OpenAIRequest.Message("user", prompt)));

        return webClient.post()
                .uri("/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAIResponse.class)
                .map(response -> response.getChoices().get(0).getMessage().getContent());
    }

}
