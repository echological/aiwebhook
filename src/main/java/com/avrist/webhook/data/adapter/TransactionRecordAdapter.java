package com.avrist.webhook.data.adapter;

import com.avrist.webhook.data.dto.TelegramChatRecordDto;
import com.avrist.webhook.data.dto.TransactionRecordDto;
import com.avrist.webhook.factory.MongoDataConnectionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class TransactionRecordAdapter {

    @Inject
    private MongoDataConnectionFactory mongo;

    @Inject
    private ObjectMapper objectMapper;

    public void save(TransactionRecordDto entity) {
        try {
            var o = entity;
            o.setUuid(UUID.randomUUID().toString());

            if(ObjectUtils.isEmpty(o.getCreatedAt())){
                o.setCreatedAt(LocalDateTime.now());
            }
            o.setUpdatedAt(LocalDateTime.now());

            var doc = Document.parse(objectMapper.writeValueAsString(o));
            mongo.collection(TransactionRecordDto.COLLECTION).insertOne(doc);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize telegram record", e);
        }
    }
}
