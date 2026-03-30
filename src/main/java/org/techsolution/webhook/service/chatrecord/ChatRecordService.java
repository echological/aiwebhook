package org.techsolution.webhook.service.chatrecord;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.techsolution.webhook.contract.ServiceContract;
import org.techsolution.webhook.data.adapter.TelegramChatRecordAdapter;
import org.techsolution.webhook.data.dto.TelegramChatRecordDto;
import org.techsolution.webhook.exception.ServiceValidationException;
import org.techsolution.webhook.service.chatrecord.dto.ChatRecordResponse;

@ApplicationScoped
public class ChatRecordService implements ServiceContract<TelegramChatRecordDto, ChatRecordResponse> {

    @Inject
    private TelegramChatRecordAdapter telegramChatRecordAdapter;

    @Override
    public ChatRecordResponse execute(TelegramChatRecordDto o) throws ServiceValidationException {
        telegramChatRecordAdapter.save(o);
        return null;
    }
}
