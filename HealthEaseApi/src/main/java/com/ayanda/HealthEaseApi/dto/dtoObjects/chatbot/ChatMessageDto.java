package com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Long id;
    private Long sessionId;
    private String sender;
    private String content;
    private LocalDateTime timestamp;
    private String messageType;
    private Map<String, Object> metadata;
}
