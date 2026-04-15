package com.avrist.webhook.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramChatRecordDto {
    public static final String COLLECTION = "telegram_chat_records";

    @JsonProperty("_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectId id;
    @JsonProperty("update_id")
    private Long updateId;
    private Message message;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    @JsonProperty("app_version")
    private String appVersion;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        @JsonProperty("message_id")
        private Long messageId;
        private From from;
        private Chat chat;
        private Long date;
        private String text;
        private String caption;
        private List<PhotoSize> photo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PhotoSize {

        @JsonProperty("file_id")
        private String fileId;

        @JsonProperty("file_unique_id")
        private String fileUniqueId;

        @JsonProperty("file_size")
        private Long fileSize;

        private Integer width;
        private Integer height;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class From {
        private Long id;
        @JsonProperty("is_bot")
        private Boolean isBot;
        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("last_name")
        private String lastName;
        private String username;
        @JsonProperty("language_code")
        private String languageCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Chat {
        private Long id;
        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("last_name")
        private String lastName;
        private String username;
        private String type;
    }
}
