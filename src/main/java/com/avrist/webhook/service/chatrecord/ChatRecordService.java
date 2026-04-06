package com.avrist.webhook.service.chatrecord;

import com.avrist.webhook.contract.ServiceContract;
import com.avrist.webhook.data.adapter.TelegramChatRecordAdapter;
import com.avrist.webhook.data.dto.TelegramChatRecordDto;
import com.avrist.webhook.dto.EmptyResponse;
import com.avrist.webhook.exception.ServiceValidationException;
import com.avrist.webhook.network.adapter.TelegramAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ChatRecordService implements ServiceContract<TelegramChatRecordDto, EmptyResponse> {

    @Inject
    private TelegramChatRecordAdapter telegramChatRecordAdapter;

    @Inject
    private TelegramAdapter telegramAdapter;

    @Override
    public EmptyResponse execute(TelegramChatRecordDto o) throws ServiceValidationException {
        telegramChatRecordAdapter.save(o);
        telegramAdapter.sendMessage(Long.toString(o.getMessage().getFrom().getId()), "acknowledge");
        return new EmptyResponse();
    }
}
