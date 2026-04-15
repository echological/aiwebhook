package com.avrist.webhook.service.chatrecord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallbackFileDto {
    private String caption;
    private File file;
    private String ocrResult;
}
