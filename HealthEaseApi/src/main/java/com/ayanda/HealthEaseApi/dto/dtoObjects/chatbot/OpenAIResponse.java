package com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenAIResponse {
    @Builder.Default
    private List<Choice> choices = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Choice {
        private Message message;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Message {
            private String role;
            private String content;
        }
    }
}
