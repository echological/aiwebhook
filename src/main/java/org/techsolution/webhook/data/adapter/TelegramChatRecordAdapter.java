package org.techsolution.webhook.data.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.techsolution.webhook.data.dto.TelegramChatRecordDto;
import org.techsolution.webhook.factory.MongoDataConnectionFactory;

@ApplicationScoped
public class TelegramChatRecordAdapter {

    @Inject
    private MongoDataConnectionFactory mongo;

    @Inject
    private ObjectMapper objectMapper;

    public void save(TelegramChatRecordDto entity) {
        try {
            var doc = Document.parse(objectMapper.writeValueAsString(entity));
            mongo.collection(TelegramChatRecordDto.COLLECTION).insertOne(doc);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize telegram record", e);
        }
    }

    public void upsert(TelegramChatRecordDto entity) {
        try {
            var doc = Document.parse(objectMapper.writeValueAsString(entity));
            mongo.collection(TelegramChatRecordDto.COLLECTION).replaceOne(
                    Filters.eq("update_id", entity.getUpdateId()),
                    doc,
                    new ReplaceOptions().upsert(true)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize telegram record", e);
        }
    }

}
