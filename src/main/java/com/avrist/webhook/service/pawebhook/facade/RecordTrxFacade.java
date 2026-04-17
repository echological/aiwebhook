package com.avrist.webhook.service.pawebhook.facade;

import com.avrist.webhook.constant.WebhookAction;
import com.avrist.webhook.data.adapter.ErrorLogAdapter;
import com.avrist.webhook.data.adapter.TransactionRecordAdapter;
import com.avrist.webhook.data.dto.TelegramChatRecordDto;
import com.avrist.webhook.data.dto.TransactionRecordDto;
import com.avrist.webhook.network.adapter.OpenAIFinancialParserAdapter;
import com.avrist.webhook.network.adapter.TelegramAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.ObjectUtils;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;

@ApplicationScoped
public class RecordTrxFacade {

    private static final Logger LOG = Logger.getLogger(RecordTrxFacade.class);

    @Inject
    private TransactionRecordAdapter transactionRecordAdapter;

    @Inject
    private OpenAIFinancialParserAdapter openAiFinancialParserAdapter;

    @Inject
    private TelegramAdapter telegramAdapter;

    @Inject
    private ErrorLogAdapter errorLogAdapter;

    public void recordTransaction(
            TelegramChatRecordDto chatRecord,
            String webhookAction,
            String ocrResult,
            String caption
    ) {
        if(!webhookAction.equalsIgnoreCase(WebhookAction.RECORD_TRX))
            return;

        try {
            var input = chatRecord.getMessage().getText();
            if(!ObjectUtils.isEmpty(ocrResult)){
                input = ocrResult;
            }

            if(ObjectUtils.isEmpty(input)){
                return;
            }

            openAiFinancialParserAdapter.getFinancialTrx(input)
                    .getData().stream().forEach(trx -> {
                        transactionRecordAdapter.save(TransactionRecordDto.builder()
                                .error(trx.getError())
                                .errorCause(trx.getErrorCause())
                                .type(trx.getType())
                                .description(trx.getDescription())
                                .amount(trx.getAmount())
                                .currency(trx.getCurrency())
                                .category(trx.getCategory())
                                .account(trx.getAccount())
                                .fromUsername(chatRecord.getMessage().getFrom().getUsername())
                                .chatText(chatRecord.getMessage().getText())
                                .chatRecordId(chatRecord.getId())
                                .ocrResult(ocrResult)
                                .caption(caption)
                                .trxDate(trx.getTrxDate())
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build());

                        if(trx.getError()){
                            telegramAdapter.sendMessage(
                                    Long.toString(chatRecord.getMessage().getFrom().getId()),
                                    String.format("Description '%s' %s %s!", trx.getDescription(), "error", trx.getErrorCause()));
                            LOG.error(String.format("Failed to get financial trx %s", trx.getErrorCause()));
                        }else{
                            telegramAdapter.sendMessage(
                                    Long.toString(chatRecord.getMessage().getFrom().getId()),
                                    String.format("Description '%s' %s!", trx.getDescription(), "acknowledge"));
                        }
                    });
        }catch (Exception e){
            telegramAdapter.sendMessage(
                    Long.toString(chatRecord.getMessage().getFrom().getId()),
                    String.format("message '%s' %s %s!", chatRecord.getMessage().getText(), "error", e.getMessage()));
            LOG.error("Failed to get financial trx", e);
            errorLogAdapter.logErrorToMongo(e, 500, "INTERNAL_ERROR");
        }
    }

}
