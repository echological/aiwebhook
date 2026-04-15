package com.avrist.webhook.service.pawebhook;

import com.avrist.webhook.config.AppConfig;
import com.avrist.webhook.constant.WebhookAction;
import com.avrist.webhook.contract.ServiceContract;
import com.avrist.webhook.data.adapter.TelegramChatRecordAdapter;
import com.avrist.webhook.dto.EmptyResponse;
import com.avrist.webhook.exception.ServiceValidationException;
import com.avrist.webhook.service.pawebhook.dto.ChatRecordRequest;
import com.avrist.webhook.helper.TelegramHelper;
import com.avrist.webhook.service.pawebhook.facade.RecordTrxFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.ObjectUtils;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PAWebhookService
        implements ServiceContract<ChatRecordRequest, EmptyResponse> {

    private static final Logger LOG = Logger.getLogger(PAWebhookService.class);

    @Inject
    private TelegramChatRecordAdapter telegramChatRecordAdapter;

    @Inject
    private AppConfig appConfig;

    @Inject
    private RecordTrxFacade recordTrxFacade;

    @Inject
    private TelegramHelper telegramHelper;

    @Override
    public EmptyResponse execute(ChatRecordRequest o) throws ServiceValidationException {
        if(ObjectUtils.isEmpty(o.getTelegramApiKey())){
            LOG.error("Telegram API Key is empty");
            return new EmptyResponse();
        }

        if(!o.getTelegramApiKey().equalsIgnoreCase(appConfig.getTelegramWebhookApiKey())){
            LOG.error("Telegram API Key mismatch");
            return new EmptyResponse();
        }

        var chatRecord = telegramChatRecordAdapter.save(o.getTelegramChatRecordDto());

        String ocrResult = null;
        String caption = null;

        try {
            var image = telegramHelper.readImageAndCaption(chatRecord);
            ocrResult = image.getOcrResult();
            caption = image.getCaption();
        }catch (Exception e){
            LOG.error("Error reading image", e);
        }

        recordTrxFacade.recordTransaction(
                chatRecord,
                WebhookAction.RECORD_TRX,
                ocrResult,
                caption
        );
        return new EmptyResponse();
    }
}
