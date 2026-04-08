package com.avrist.webhook.service.chatrecord;

import com.avrist.webhook.contract.ServiceContract;
import com.avrist.webhook.data.adapter.TelegramChatRecordAdapter;
import com.avrist.webhook.data.dto.TelegramChatRecordDto;
import com.avrist.webhook.dto.EmptyResponse;
import com.avrist.webhook.exception.ServiceValidationException;
import com.avrist.webhook.network.adapter.TelegramAdapter;
import com.avrist.webhook.service.chatrecord.dto.ChatRecordRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ChatRecordService
        implements ServiceContract<ChatRecordRequest, EmptyResponse> {

    @Inject
    private TelegramChatRecordAdapter telegramChatRecordAdapter;

    @Inject
    private TelegramAdapter telegramAdapter;

    @Override
    public EmptyResponse execute(ChatRecordRequest o) throws ServiceValidationException {
        telegramChatRecordAdapter.save(o.getTelegramChatRecordDto());
        telegramAdapter.sendMessage(
                Long.toString(o.getTelegramChatRecordDto().getMessage().getFrom().getId()),
                String.format("message '%s' %s!", o.getTelegramChatRecordDto().getMessage().getText(), "acknowledge"));
        return new EmptyResponse();
    }
}
