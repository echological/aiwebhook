package com.avrist.webhook.util;

import lombok.experimental.UtilityClass;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility helper untuk operasi OCR berbasis Tesseract.
 *
 * <p>Class ini menyediakan helper stateless untuk:</p>
 * <ul>
 *     <li>membuat instance {@link ITesseract}</li>
 *     <li>memvalidasi lokasi folder {@code tessdata}</li>
 *     <li>mengekstrak text dari image dalam bentuk {@code byte[]}</li>
 *     <li>menyediakan default path Windows untuk instalasi Tesseract umum</li>
 * </ul>
 *
 * <p>Catatan penting:</p>
 * <ul>
 *     <li>parameter {@code tessdataPath} harus menunjuk ke folder {@code tessdata}, bukan ke executable {@code tesseract.exe}</li>
 *     <li>hasil OCR sangat bergantung pada kualitas image dan ketersediaan language data</li>
 *     <li>utility ini cocok sebagai helper level rendah, lalu dipakai oleh adapter/service yang membaca konfigurasi aplikasi</li>
 * </ul>
 */
@UtilityClass
public class TesseractUtil {

    /**
     * Bahasa default Tesseract yang digunakan ketika parameter bahasa kosong.
     *
     * <p>Nilai default menggabungkan English dan Indonesian agar hasil OCR lebih fleksibel
     * untuk input campuran.</p>
     */
    public static final String DEFAULT_LANGUAGE = "eng+ind";

    /**
     * Membuat instance {@link ITesseract} baru dengan konfigurasi {@code tessdataPath}
     * dan bahasa OCR yang diberikan.
     *
     * <p>Method ini akan memvalidasi terlebih dahulu bahwa path yang diberikan valid
     * dan merupakan directory yang dapat digunakan sebagai lokasi {@code tessdata}.</p>
     *
     * @param tessdataPath path ke folder {@code tessdata}
     * @param language bahasa OCR yang akan dipakai; jika null atau blank maka akan
     *                 menggunakan {@link #DEFAULT_LANGUAGE}
     * @return instance {@link ITesseract} yang siap dipakai untuk OCR
     * @throws IllegalArgumentException jika {@code tessdataPath} kosong, tidak ada,
     *                                  atau bukan directory
     */
    public ITesseract create(String tessdataPath, String language) {
        validateTessdataPath(tessdataPath);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessdataPath);
        tesseract.setLanguage(isBlank(language) ? DEFAULT_LANGUAGE : language.trim());

        return tesseract;
    }

    /**
     * Mengekstrak text dari image menggunakan bahasa default
     * {@link #DEFAULT_LANGUAGE}.
     *
     * @param imageBytes isi file image dalam bentuk byte array
     * @param tessdataPath path ke folder {@code tessdata}
     * @return text hasil OCR, sudah di-{@code trim}; mengembalikan string kosong
     *         jika engine mengembalikan null
     * @throws IllegalArgumentException jika image kosong, invalid, atau format tidak didukung,
     *                                  maupun jika {@code tessdataPath} tidak valid
     * @throws RuntimeException jika terjadi kegagalan OCR selain validasi input
     */
    public String extractText(byte[] imageBytes, String tessdataPath) {
        return extractText(imageBytes, tessdataPath, DEFAULT_LANGUAGE);
    }

    /**
     * Mengekstrak text dari image menggunakan Tesseract dengan bahasa yang ditentukan.
     *
     * <p>Flow method ini:</p>
     * <ol>
     *     <li>validasi bahwa {@code imageBytes} tidak kosong</li>
     *     <li>parse image bytes menjadi {@link BufferedImage}</li>
     *     <li>buat instance Tesseract melalui {@link #create(String, String)}</li>
     *     <li>jalankan OCR dan kembalikan hasilnya dalam bentuk string yang sudah di-trim</li>
     * </ol>
     *
     * @param imageBytes isi file image dalam bentuk byte array
     * @param tessdataPath path ke folder {@code tessdata}
     * @param language bahasa OCR yang akan dipakai; jika null atau blank maka akan
     *                 menggunakan {@link #DEFAULT_LANGUAGE}
     * @return text hasil OCR, sudah di-{@code trim}; mengembalikan string kosong
     *         jika engine mengembalikan null
     * @throws IllegalArgumentException jika {@code imageBytes} kosong, image tidak valid,
     *                                  format tidak didukung, atau {@code tessdataPath} tidak valid
     * @throws RuntimeException jika terjadi error saat proses OCR dijalankan
     */
    public String extractText(byte[] imageBytes, String tessdataPath, String language) {
        try {
            if (imageBytes == null || imageBytes.length == 0) {
                throw new IllegalArgumentException("Image bytes tidak boleh kosong");
            }

            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (bufferedImage == null) {
                throw new IllegalArgumentException("Image tidak valid atau format tidak didukung");
            }

            ITesseract tesseract = create(tessdataPath, language);
            String result = tesseract.doOCR(bufferedImage);

            return result == null ? "" : result.trim();
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Gagal melakukan OCR dengan Tesseract", e);
        }
    }

    /**
     * Memvalidasi bahwa path yang diberikan adalah folder {@code tessdata} yang valid.
     *
     * <p>Method ini hanya memeriksa:</p>
     * <ul>
     *     <li>path tidak null / blank</li>
     *     <li>path tersedia di filesystem</li>
     *     <li>path adalah directory</li>
     * </ul>
     *
     * <p>Method ini belum memverifikasi keberadaan file language seperti
     * {@code eng.traineddata} atau {@code ind.traineddata}.</p>
     *
     * @param tessdataPath path ke folder {@code tessdata}
     * @throws IllegalArgumentException jika path kosong, tidak ditemukan,
     *                                  atau bukan directory
     */
    public void validateTessdataPath(String tessdataPath) {
        if (isBlank(tessdataPath)) {
            throw new IllegalArgumentException("Tessdata path tidak boleh kosong");
        }

        Path path = Path.of(tessdataPath);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Tessdata path tidak ditemukan: " + tessdataPath);
        }

        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Tessdata path harus berupa directory: " + tessdataPath);
        }
    }

    /**
     * Mengembalikan default lokasi {@code tessdata} untuk instalasi Tesseract umum di Windows.
     *
     * <p>Path yang dicek adalah:</p>
     * <pre>{@code
     * C:/Program Files/Tesseract-OCR/tessdata
     * }</pre>
     *
     * @return path default Windows ke folder {@code tessdata}
     * @throws IllegalStateException jika path default tersebut tidak ditemukan
     *                               atau bukan directory
     */
    public String resolveDefaultWindowsTessdataPath() {
        Path defaultPath = Path.of("C:/Program Files/Tesseract-OCR/tessdata");
        if (Files.exists(defaultPath) && Files.isDirectory(defaultPath)) {
            return defaultPath.toString();
        }

        throw new IllegalStateException("Default tessdata path Windows tidak ditemukan: " + defaultPath);
    }

    /**
     * Mengecek apakah string bernilai null, kosong, atau hanya berisi whitespace.
     *
     * @param value nilai string yang akan diperiksa
     * @return {@code true} jika null atau blank, {@code false} jika berisi karakter non-whitespace
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}