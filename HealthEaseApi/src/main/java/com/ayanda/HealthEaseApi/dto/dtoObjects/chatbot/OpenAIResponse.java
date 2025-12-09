package com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String id;
    private String object;
    private Long created;
    private String model;
    @Builder.Default
    private List<Choice> choices = new ArrayList<>();
    private Usage usage;
    @JsonProperty("service_tier")
    private String serviceTier;
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Choice {
        private Integer index;
        private Message message;
        @JsonProperty("finish_reason")
        private String finishReason;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Message {
            private String role;
            private String content;
            @JsonProperty("function_call")
            private FunctionCall functionCall;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class FunctionCall {
            private String name;
            private String arguments;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;
        @JsonProperty("completion_tokens")
        private Integer completionTokens;
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
