package com.ayanda.HealthEaseApi.controller;

import com.ayanda.HealthEaseApi.dto.dtoObjects.ApiResponseDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.ResourceNotFoundException;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.UnauthorizedException;
import com.ayanda.HealthEaseApi.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/chatbot/openai")
public class OpenAIController {

    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @GetMapping("service/{userId}/ask")
    public ResponseEntity<ApiResponseDto> ask(@PathVariable Long userId,@RequestParam String prompt) {
        try {
            Mono<String> response = openAIService.getResponse(prompt);
            return ResponseEntity.ok(new ApiResponseDto(true, "successfull response from openai", response));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }
}
