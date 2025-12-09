package com.ayanda.HealthEaseApi.controller;

import com.ayanda.HealthEaseApi.dto.dtoObjects.ApiResponseDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot.OpenAIRequest;
import com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot.OpenAIResponse;
import com.ayanda.HealthEaseApi.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/chatbot")
@RequiredArgsConstructor
public class OpenAIController {

    private final OpenAIService openAIService;

    @PostMapping("{userId}/chat")
    public ResponseEntity<ApiResponseDto> chat(@PathVariable Long userId, @RequestBody String chatRequest) {
        try {
            OpenAIRequest request = OpenAIRequest.builder()
                    .model("gpt-3.5-turbo")
                    .messages(List.of(
                            OpenAIRequest.Message.builder()
                                    .role("user")
                                    .content(chatRequest)
                                    .build()
                    ))
                    .maxTokens(500)
                    .build();

            OpenAIResponse response = openAIService.getResponse(request);
            String aiResponse = response.getChoices().get(0).getMessage().getContent();

            return ResponseEntity.ok(new ApiResponseDto(true, "Success", aiResponse));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred: " + e.getMessage()));
        }
    }
}
