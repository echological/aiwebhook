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
public class OpenAIInstructionParserAdapter {

    @Inject
    private AppConfig appConfig;

    @Inject
    private ConfigAdapter configAdapter;

    private static final String DEFAULT_INSTRUCTION = """
            Tugasmu mengekstrak data transaksi keuangan dari input user.
            Jika input BUKAN transaksi keuangan (tidak ada intent pemasukan/pengeluaran atau nominal transaksi),
            set error=true dan isi error_cause.
            Dalam kondisi error, isi field lain dengan nilai default aman:
            type='expense', description='', amount=0, currency='IDR', category='unknown', account=null.
            Selalu output JSON valid sesuai schema, tanpa teks tambahan.
            """;

    public FinancialTrxSchema getFinancialTrx(String input){
        var client = OpenAIOkHttpClient.builder()
                .apiKey(appConfig.getOpenaiApiKey())
                .build();

        var params = ResponseCreateParams.builder()
                .model(ChatModel.of(OpenAiModel.GPT_5_4_NANO))
                .instructions(configAdapter.load("type_parser_instruction", DEFAULT_INSTRUCTION))
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
