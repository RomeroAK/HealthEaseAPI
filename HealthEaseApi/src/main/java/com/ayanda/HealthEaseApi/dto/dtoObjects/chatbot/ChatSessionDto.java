package com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionDto {
    private Long id;
    private Long userId;
    private LocalDateTime sessionStart;
    private LocalDateTime sessionEnd;
    private Boolean active;
}
