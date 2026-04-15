package com.avrist.webhook.data.adapter;

import com.avrist.webhook.config.AppConfig;
import com.avrist.webhook.data.dto.TransactionRecordDto;
import com.avrist.webhook.factory.MongoDataConnectionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@ApplicationScoped
public class TransactionRecordAdapter {

    @Inject
    private MongoDataConnectionFactory mongo;

    @Inject
    private AppConfig appConfig;

    @Inject
    private ObjectMapper objectMapper;

    public void save(TransactionRecordDto entity) {
        try {
            var o = entity;
            o.setAppVersion(appConfig.getAppVersion());

            if(ObjectUtils.isEmpty(o.getId())){
                o.setId(new ObjectId());
            }

            if(ObjectUtils.isEmpty(o.getCreatedAt())){
                o.setCreatedAt(LocalDateTime.now());
            }
            o.setUpdatedAt(LocalDateTime.now());

            var doc = Document.parse(objectMapper.writeValueAsString(o));
            doc.put("_id", o.getId());
            doc.put("telegram_chat_records_id", o.getChatRecordId());

            mongo.collection(TransactionRecordDto.COLLECTION).insertOne(doc);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize trx record", e);
        }
    }
}
