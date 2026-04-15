package com.avrist.webhook.helper;

import com.avrist.webhook.config.AppConfig;
import com.avrist.webhook.data.dto.TelegramChatRecordDto;
import com.avrist.webhook.network.adapter.TelegramAdapter;
import com.avrist.webhook.service.pawebhook.dto.CallbackFileDto;
import com.avrist.webhook.util.TesseractUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class TelegramHelper {

    @Inject
    private TelegramAdapter telegramAdapter;

    @Inject
    private AppConfig appConfig;

    public CallbackFileDto readImageAndCaption(TelegramChatRecordDto chatRecord) throws IOException {
        var biggestPhoto = chatRecord.getMessage().getPhoto().stream()
                .max(java.util.Comparator.comparingLong(photo ->
                        (long) photo.getWidth() * photo.getHeight()));

        if(ObjectUtils.isEmpty(biggestPhoto))
            return null;

        var telegramFile = telegramAdapter.getFile(biggestPhoto.get().getFileId());
        var file = telegramAdapter.downloadFile(
                biggestPhoto.get().getFileId(),
                telegramFile.getResult().getFilePath());

        var image = Files.readAllBytes(file.toPath());
        var tessdataPath = Path.of(appConfig.getTessData());

        var result = TesseractUtil.extractText(image, tessdataPath.toString());

        return CallbackFileDto.builder()
                .caption(chatRecord.getMessage().getCaption())
                .file(file)
                .ocrResult(result)
                .build();
    }

}
