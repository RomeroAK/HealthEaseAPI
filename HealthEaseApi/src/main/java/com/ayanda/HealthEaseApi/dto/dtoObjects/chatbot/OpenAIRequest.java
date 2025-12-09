package com.ayanda.HealthEaseApi.dto.dtoObjects.chatbot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenAIRequest {

        private String model;
        private List<Message> messages;
        @JsonProperty("max_tokens")
        private Integer maxTokens;
        private List<Function> functions;
        @JsonProperty("function_call")
        private Object functionCall; // Can be "auto", "none", or {"name": "function_name"}

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

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Function {
                private String name;
                private String description;
                private Parameters parameters;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Parameters {
                private String type;
                private Map<String, Property> properties;
                private List<String> required;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Property {
                private String type;
                private String description;
                @JsonProperty("enum")
                private List<String> enumValues;
        }
}
