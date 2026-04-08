package com.avrist.webhook.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAiRequest {

    private String model;
    private String instructions;
    private String input;
    private Text text;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Text {
        private Format format;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Format {
        private String type;   // json_schema
        private String name;   // financial_transaction
        private Schema schema;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Schema {
        private String type; // object
        private Map<String, SchemaProperty> properties;
        private List<String> required;
        @JsonProperty("additionalProperties")
        private Boolean additionalProperties;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SchemaProperty {
        private Object type;
        @JsonProperty("enum")
        private List<String> enumValues;
    }

}
