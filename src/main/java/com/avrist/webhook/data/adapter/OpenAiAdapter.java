package com.avrist.webhook.data.adapter;


import com.avrist.webhook.etc.FinancialTrxSchema;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.StructuredResponseCreateParams;

public class OpenAiAdapter {

    public void sendChat(){
        // Ambil API key dari env var OPENAI_API_KEY
        OpenAIClient client = OpenAIOkHttpClient.fromEnv();
        String instructions = """
            Tugasmu mengekstrak data transaksi keuangan dari input user.
            Jika input BUKAN transaksi keuangan (tidak ada intent pemasukan/pengeluaran atau nominal transaksi),
            set error=true dan isi error_cause.
            Dalam kondisi error, isi field lain dengan nilai default aman:
            type='expense', description='', amount=0, currency='IDR', category='unknown', account=null.
            Selalu output JSON valid sesuai schema, tanpa teks tambahan.
            """;
        StructuredResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.of("gpt-5")) // aman walau constant belum ada
                .instructions(instructions)
                .input("nama saya batman")
                .text(FinancialTrxSchema.class) // SDK generate schema dari class
                .build();
        FinancialTrxSchema result = client.responses().create(params).output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Output kosong"));

        System.out.println("error       = " + result.error);
        System.out.println("error_cause = " + result.errorCause);
        System.out.println("type        = " + result.type);
        System.out.println("amount      = " + result.amount);

    }

}
