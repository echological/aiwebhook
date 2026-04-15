package com.avrist.webhook.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Disabled
class TesseractUtilTest {

    @Test
    @Disabled
    void extractText() throws IOException {
        var image = Files.readAllBytes(Path.of("testfile/istockphoto-1353555259-1024x1024.jpg"));
        var tessdataPath = Path.of("C:/Users/dsnp124/AppData/Local/Programs/Tesseract-OCR/tessdata");

        var result = TesseractUtil.extractText(image, tessdataPath.toString());
        Assertions.fail();
    }

}