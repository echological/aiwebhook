package com.avrist.webhook.network.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramGetFileDto {

    private boolean ok;

    private Result result;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        @JsonProperty("file_id")
        private String fileId;

        @JsonProperty("file_unique_id")
        private String fileUniqueId;

        @JsonProperty("file_size")
        private Long fileSize;

        @JsonProperty("file_path")
        private String filePath;
    }
}