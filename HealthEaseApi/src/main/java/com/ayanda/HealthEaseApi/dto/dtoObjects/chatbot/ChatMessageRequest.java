package com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {
    private String message;
    private Long sessionId;
}
