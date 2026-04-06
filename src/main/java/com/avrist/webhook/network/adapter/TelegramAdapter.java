package com.avrist.webhook.network.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import com.avrist.webhook.config.AppConfig;
import com.avrist.webhook.network.dto.TelegramSendMessageResponseDto;

import java.io.IOException;

@ApplicationScoped
public class TelegramAdapter {

    private static final String SEND_MESSAGE_ENDPOINT = "/sendMessage";

    @Inject
    private AppConfig appConfig;

    @Inject
    private ObjectMapper objectMapper;

    private String buildSendMessageUrl() {
        var baseUrl = appConfig.getTelegramBotBaseUrl();
        var botToken = appConfig.getTelegramBotToken();
        return String.format("%s/bot%s%s", baseUrl, botToken, SEND_MESSAGE_ENDPOINT);
    }

    public TelegramSendMessageResponseDto sendMessage(String chatId, String text) {
        if (chatId == null || chatId.isBlank()) {
            throw new IllegalArgumentException("chatId wajib diisi");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("text wajib diisi");
        }
        String url = buildSendMessageUrl();
        try {
            HttpResponse<String> response = Unirest.post(url)
                    .field("chat_id", chatId)
                    .field("text", text)
                    .asString();

            if (response.getStatus() < 200 || response.getStatus() >= 300) {
                throw new RuntimeException("Telegram API error " + response.getStatus() + ": " + response.getBody());
            }
            TelegramSendMessageResponseDto dto = objectMapper.readValue(response.getBody(), TelegramSendMessageResponseDto.class);
            if (!Boolean.TRUE.equals(dto.getOk())) {
                throw new RuntimeException("Telegram API returned ok=false: " + response.getBody());
            }
            return dto;
        } catch (IOException e) {
            throw new RuntimeException("Gagal parse response Telegram", e);
        }
    }

}
