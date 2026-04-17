package com.avrist.webhook.data.adapter;

import com.avrist.webhook.config.AppConfig;
import com.avrist.webhook.data.dto.TelegramChatRecordDto;
import com.avrist.webhook.factory.MongoDataConnectionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@ApplicationScoped
public class TelegramChatRecordAdapter {

    @Inject
    private MongoDataConnectionFactory mongo;

    @Inject
    private AppConfig appConfig;

    @Inject
    private ObjectMapper objectMapper;

    public TelegramChatRecordDto save(TelegramChatRecordDto entity) {
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

            mongo.collection(TelegramChatRecordDto.COLLECTION).insertOne(doc);
            return o;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize telegram record", e);
        }
    }

    public void upsert(TelegramChatRecordDto entity) {
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

            mongo.collection(TelegramChatRecordDto.COLLECTION).replaceOne(
                    Filters.eq("update_id", o.getUpdateId()),
                    doc,
                    new ReplaceOptions().upsert(true)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize telegram record", e);
        }
    }

}
