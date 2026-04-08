package com.avrist.webhook.service.chatrecord;

import com.avrist.webhook.config.AppConfig;
import com.avrist.webhook.contract.ServiceContract;
import com.avrist.webhook.data.adapter.TelegramChatRecordAdapter;
import com.avrist.webhook.dto.EmptyResponse;
import com.avrist.webhook.exception.ServiceValidationException;
import com.avrist.webhook.network.adapter.TelegramAdapter;
import com.avrist.webhook.service.chatrecord.dto.ChatRecordRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ChatRecordService
        implements ServiceContract<ChatRecordRequest, EmptyResponse> {

    private static final Logger LOG = Logger.getLogger(ChatRecordService.class);

    @Inject
    private TelegramChatRecordAdapter telegramChatRecordAdapter;

    @Inject
    private TelegramAdapter telegramAdapter;

    @Inject
    private AppConfig appConfig;

    @Override
    public EmptyResponse execute(ChatRecordRequest o) throws ServiceValidationException {
        if(!o.getTelegramApiKey().equalsIgnoreCase(appConfig.getTelegramWebhookApiKey())){
            LOG.error("Telegram API Key mismatch");
            return new EmptyResponse();
        }

        telegramChatRecordAdapter.save(o.getTelegramChatRecordDto());
        telegramAdapter.sendMessage(
                Long.toString(o.getTelegramChatRecordDto().getMessage().getFrom().getId()),
                String.format("message '%s' %s!", o.getTelegramChatRecordDto().getMessage().getText(), "acknowledge"));
        return new EmptyResponse();
    }
}
