package org.techsolution.webhook.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.techsolution.webhook.dto.TelegramUpdateDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramChatRecordEntity {

    @JsonProperty("update_id")
    private Long updateId;

    @JsonProperty("message_id")
    private Long messageId;

    private Long fromId;
    private Boolean fromIsBot;
    private String fromFirstName;
    private String fromLastName;
    private String fromUsername;
    private String fromLanguageCode;

    private Long chatId;
    private String chatFirstName;
    private String chatLastName;
    private String chatUsername;
    private String chatType;

    private Long messageDate;
    private String text;

    public static TelegramChatRecordEntity from(TelegramUpdateDto dto) {
        if (dto == null || dto.getMessage() == null) {
            return TelegramChatRecordEntity.builder().build();
        }

        TelegramUpdateDto.Message message = dto.getMessage();
        TelegramUpdateDto.From from = message.getFrom();
        TelegramUpdateDto.Chat chat = message.getChat();

        return TelegramChatRecordEntity.builder()
                .updateId(dto.getUpdateId())
                .messageId(message.getMessageId())
                .fromId(from != null ? from.getId() : null)
                .fromIsBot(from != null ? from.getIsBot() : null)
                .fromFirstName(from != null ? from.getFirstName() : null)
                .fromLastName(from != null ? from.getLastName() : null)
                .fromUsername(from != null ? from.getUsername() : null)
                .fromLanguageCode(from != null ? from.getLanguageCode() : null)
                .chatId(chat != null ? chat.getId() : null)
                .chatFirstName(chat != null ? chat.getFirstName() : null)
                .chatLastName(chat != null ? chat.getLastName() : null)
                .chatUsername(chat != null ? chat.getUsername() : null)
                .chatType(chat != null ? chat.getType() : null)
                .messageDate(message.getDate())
                .text(message.getText())
                .build();
    }
}
