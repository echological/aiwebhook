package org.techsolution.webhook.network.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.techsolution.webhook.config.AppConfig;
import org.techsolution.webhook.network.dto.TelegramSendMessageResponseDto;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@ApplicationScoped
public class TelegramAdapter {

    private static final String SEND_MESSAGE_ENDPOINT = "/sendMessage";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Inject
    private AppConfig appConfig;

    @Inject
    private ObjectMapper objectMapper;

    private String buildSendMessageUrl() {
        var baseUrl = appConfig.getTelegramBotBaseUrl();
        var botToken = appConfig.getTelegramBotToken();
        return String.format("%s%s%s", baseUrl, "/bot", botToken, SEND_MESSAGE_ENDPOINT);
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public TelegramSendMessageResponseDto sendMessage(String chatId, String text) {
        if (chatId == null || chatId.isBlank()) {
            throw new IllegalArgumentException("chatId wajib diisi");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("text wajib diisi");
        }
        String url = buildSendMessageUrl();
        String body = "chat_id=" + encode(chatId) + "&text=" + encode(text);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException("Telegram API error " + response.statusCode() + ": " + response.body());
            }
            TelegramSendMessageResponseDto dto =
                    objectMapper.readValue(response.body(), TelegramSendMessageResponseDto.class);
            if (!Boolean.TRUE.equals(dto.getOk())) {
                throw new RuntimeException("Telegram API returned ok=false: " + response.body());
            }
            return dto;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request ke Telegram ter-interrupt", e);
        } catch (IOException e) {
            throw new RuntimeException("Gagal parse response Telegram", e);
        }
    }

}
