package com.avrist.webhook.network.adapter;


import com.avrist.webhook.config.AppConfig;
import com.avrist.webhook.constant.OpenAiModel;
import com.avrist.webhook.data.adapter.ConfigAdapter;
import com.avrist.webhook.etc.FinancialTrxSchema;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.ResponseCreateParams;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OpenAIFinancialParserAdapter {

    @Inject
    private AppConfig appConfig;

    @Inject
    private ConfigAdapter configAdapter;

    private static final String DEFAULT_INSTRUCTION = """
        Tugasmu mengekstrak data transaksi keuangan dari input user atau hasil OCR screenshot mutasi/bank/e-wallet.
        
        Aturan umum:
        1. Output harus selalu JSON valid yang sesuai schema, tanpa teks tambahan.
        2. Jika ada lebih dari satu transaksi, return array berisi beberapa object transaksi.
        3. Jika hanya satu transaksi, tetap return array dengan 1 item.
        4. Jika input bukan transaksi keuangan atau tidak ada transaksi yang bisa diidentifikasi, return array dengan 1 item error.
        5. Jangan mengarang data yang tidak ada. Jika field tidak diketahui, isi null atau error sesuai konteks.
        6. Input bisa berupa hasil OCR yang noisy, typo, terpotong, atau bercampur dengan teks UI aplikasi.
        
        Aturan parsing OCR:
        1. Abaikan teks non-transaksi seperti judul halaman, menu, tombol, saldo aktif, label UI, jumlah item, dan heading bulan/tahun jika itu bukan transaksi.
        2. Baris-baris yang berdekatan bisa mewakili 1 transaksi yang sama, misalnya:
           - satu baris berisi nama merchant + nominal
           - baris berikutnya berisi tanggal + jenis transaksi
        3. Jika ada beberapa ukuran informasi yang tampak terkait, gabungkan menjadi satu transaksi yang paling masuk akal.
        4. Jangan anggap heading seperti "HISTORY", "Transactions", "Active Balance", "Download", "See all transactions", atau nama bulan saja sebagai transaksi.
        5. Jika nominal ditemukan, gunakan sebagai amount numerik tanpa simbol dan tanpa separator ribuan.
        6. Jika currency tertulis IDR, isi currency = "IDR".
        7. Jika tanggal ditemukan, parse ke field trxDate. Jika tanggal tidak lengkap atau ambigu, isi null.
        8. Pertahankan description dari nama merchant/nama lawan transaksi/label transaksi yang paling relevan.
        9. Tentukan type secara masuk akal dari kata-kata seperti Incoming, Transfer, Top Up, Cost, Tax, Installment, Purchase, Debit, Credit.
        10. Jika OCR mengandung typo kecil, lakukan interpretasi sewajarnya, tetapi jangan terlalu agresif mengoreksi.
        
        Aturan error:
        1. Jika teks benar-benar bukan transaksi keuangan, return 1 item dengan error=true.
        2. Jika ada transaksi yang tampak valid, utamakan ekstraksi transaksi daripada error global.
        3. Jika sebagian transaksi ambigu, transaksi yang jelas tetap diekstrak.
        
        Prioritas utama:
        - tangkap transaksi yang nyata
        - abaikan noise
        - jangan halusinasi
        - output harus konsisten dengan schema
        """;

    public FinancialTrxSchema getFinancialTrx(String input){
        var client = OpenAIOkHttpClient.builder()
                .apiKey(appConfig.getOpenaiApiKey())
                .build();

        var params = ResponseCreateParams.builder()
                .model(ChatModel.of(OpenAiModel.GPT_5_4_NANO))
                .instructions(configAdapter.load("financial_trx_instruction", DEFAULT_INSTRUCTION))
                .input(input)
                .text(FinancialTrxSchema.class)
                .build();
        var result = client.responses().create(params).output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Output kosong"));
        return result;

    }

}
