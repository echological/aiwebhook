package com.avrist.webhook.network.adapter;

import com.avrist.webhook.network.dto.TelegramGetFileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import com.avrist.webhook.config.AppConfig;
import com.avrist.webhook.network.dto.TelegramSendMessageResponseDto;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class TelegramAdapter {

    private static final String SEND_MESSAGE_ENDPOINT = "/sendMessage";
    private static final String GET_FILE_ENDPOINT = "/getFile";

    @Inject
    private AppConfig appConfig;

    @Inject
    private ObjectMapper objectMapper;

    private String buildSendMessageUrl() {
        var baseUrl = appConfig.getTelegramBotBaseUrl();
        var botToken = appConfig.getTelegramBotToken();
        return String.format("%s/bot%s%s", baseUrl, botToken, SEND_MESSAGE_ENDPOINT);
    }

    private String buildGetFileUrl() {
        var baseUrl = appConfig.getTelegramBotBaseUrl();
        var botToken = appConfig.getTelegramBotToken();
        return String.format("%s/bot%s%s", baseUrl, botToken, GET_FILE_ENDPOINT);
    }

    private String buildDownloadFileUrl(String filePath) {
        var baseUrl = appConfig.getTelegramBotBaseUrl();
        var botToken = appConfig.getTelegramBotToken();
        return String.format("%s/file/bot%s/%s", baseUrl, botToken, filePath);
    }

    private String resolveExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex == filePath.length() - 1) {
            return ".tmp";
        }
        return filePath.substring(lastDotIndex);
    }

    private String sanitizeFileId(String fileId) {
        if (fileId == null || fileId.isBlank()) {
            return "file";
        }
        return fileId.replaceAll("[^a-zA-Z0-9-_]", "_");
    }

    public TelegramGetFileDto getFile(String fileId) {
        if (fileId == null || fileId.isBlank()) {
            throw new IllegalArgumentException("fileId wajib diisi");
        }

        String url = buildGetFileUrl();
        try {
            HttpResponse<String> response = Unirest.post(url)
                    .field("file_id", fileId)
                    .asString();

            if (response.getStatus() < 200 || response.getStatus() >= 300) {
                throw new RuntimeException("Telegram API error " + response.getStatus() + ": " + response.getBody());
            }

            TelegramGetFileDto dto = objectMapper.readValue(response.getBody(), TelegramGetFileDto.class);
            if (!dto.isOk()) {
                throw new RuntimeException("Telegram API returned ok=false: " + response.getBody());
            }

            return dto;
        } catch (IOException e) {
            throw new RuntimeException("Gagal parse response Telegram", e);
        }
    }

    public File downloadFile(String fileId, String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("filePath wajib diisi");
        }

        String url = buildDownloadFileUrl(filePath);
        HttpResponse<byte[]> response = Unirest.get(url).asBytes();

        if (response.getStatus() < 200 || response.getStatus() >= 300) {
            throw new RuntimeException("Telegram file download error " + response.getStatus());
        }

        try {
            String extension = resolveExtension(filePath);
            Path tempFile = Files.createTempFile("telegram-" + sanitizeFileId(fileId) + "-", extension);
            Files.write(tempFile, response.getBody());
            return tempFile.toFile();
        } catch (IOException e) {
            throw new RuntimeException("Gagal menyimpan file Telegram ke temporary file", e);
        }
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
