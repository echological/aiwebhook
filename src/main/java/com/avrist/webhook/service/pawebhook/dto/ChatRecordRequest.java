package com.avrist.webhook.service.pawebhook.dto;

import com.avrist.webhook.data.dto.TelegramChatRecordDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRecordRequest {
    private String telegramApiKey;
    private TelegramChatRecordDto telegramChatRecordDto;
}
