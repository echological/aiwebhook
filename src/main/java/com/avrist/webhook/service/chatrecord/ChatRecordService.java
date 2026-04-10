package com.avrist.webhook.service.chatrecord;

import com.avrist.webhook.config.AppConfig;
import com.avrist.webhook.contract.ServiceContract;
import com.avrist.webhook.data.adapter.ErrorLogAdapter;
import com.avrist.webhook.data.adapter.TelegramChatRecordAdapter;
import com.avrist.webhook.data.adapter.TransactionRecordAdapter;
import com.avrist.webhook.data.dto.TransactionRecordDto;
import com.avrist.webhook.dto.EmptyResponse;
import com.avrist.webhook.exception.ServiceValidationException;
import com.avrist.webhook.network.adapter.OpenAIAdapter;
import com.avrist.webhook.network.adapter.TelegramAdapter;
import com.avrist.webhook.service.chatrecord.dto.ChatRecordRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;

@ApplicationScoped
public class ChatRecordService
        implements ServiceContract<ChatRecordRequest, EmptyResponse> {

    private static final Logger LOG = Logger.getLogger(ChatRecordService.class);

    @Inject
    private TelegramChatRecordAdapter telegramChatRecordAdapter;

    @Inject
    private TransactionRecordAdapter transactionRecordAdapter;

    @Inject
    private TelegramAdapter telegramAdapter;

    @Inject
    private OpenAIAdapter openAiAdapter;

    @Inject
    private ErrorLogAdapter errorLogAdapter;

    @Inject
    private AppConfig appConfig;

    @Override
    public EmptyResponse execute(ChatRecordRequest o) throws ServiceValidationException {
        if(!o.getTelegramApiKey().equalsIgnoreCase(appConfig.getTelegramWebhookApiKey())){
            LOG.error("Telegram API Key mismatch");
            return new EmptyResponse();
        }

        var chatRecord = telegramChatRecordAdapter.save(o.getTelegramChatRecordDto());

        try {
            var trx = openAiAdapter.getFinancialTrx(o.getTelegramChatRecordDto().getMessage().getText());
            transactionRecordAdapter.save(TransactionRecordDto.builder()
                            .error(trx.getError())
                            .errorCause(trx.getErrorCause())
                            .type(trx.getType())
                            .description(trx.getDescription())
                            .amount(trx.getAmount())
                            .currency(trx.getCurrency())
                            .category(trx.getCategory())
                            .account(trx.getAccount())
                            .fromUsername(o.getTelegramChatRecordDto().getMessage().getFrom().getUsername())
                            .chatRecordUUID(chatRecord.getUuid())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                    .build());

            if(trx.getError()){
                telegramAdapter.sendMessage(
                        Long.toString(o.getTelegramChatRecordDto().getMessage().getFrom().getId()),
                        String.format("message '%s' %s %s!", o.getTelegramChatRecordDto().getMessage().getText(), "error", trx.getErrorCause()));
                LOG.error(String.format("Failed to get financial trx %s", trx.getErrorCause()));
                return new EmptyResponse();
            }
        }catch (Exception e){
            telegramAdapter.sendMessage(
                    Long.toString(o.getTelegramChatRecordDto().getMessage().getFrom().getId()),
                    String.format("message '%s' %s %s!", o.getTelegramChatRecordDto().getMessage().getText(), "error", e.getMessage()));
            LOG.error("Failed to get financial trx", e);
            errorLogAdapter.logErrorToMongo(e, 500, "INTERNAL_ERROR");
            return new EmptyResponse();
        }


        telegramAdapter.sendMessage(
                Long.toString(o.getTelegramChatRecordDto().getMessage().getFrom().getId()),
                String.format("message '%s' %s!", o.getTelegramChatRecordDto().getMessage().getText(), "acknowledge"));
        return new EmptyResponse();
    }
}
