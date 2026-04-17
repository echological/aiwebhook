package com.avrist.webhook.data.adapter;

import com.avrist.webhook.config.AppConfig;
import com.avrist.webhook.data.dto.ConfigDto;
import com.avrist.webhook.data.dto.TelegramChatRecordDto;
import com.avrist.webhook.factory.MongoDataConnectionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Filters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class ConfigAdapter {

    private static final Logger LOG = Logger.getLogger(ConfigAdapter.class);

    @Inject
    private MongoDataConnectionFactory mongo;

    @Inject
    private AppConfig appConfig;

    @Inject
    private ObjectMapper objectMapper;

    public String load(String key, String defaultValue) {
        try {
            Document doc = mongo.collection(ConfigDto.COLLECTION)
                    .find(Filters.eq("key", key))
                    .first();

            if (doc == null) {
                var configDto = ConfigDto.builder()
                        .key(key)
                        .value(defaultValue)
                        .updatedAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .appVersion(appConfig.getAppVersion())
                        .build();

                doc = Document.parse(objectMapper.writeValueAsString(configDto));
                mongo.collection(ConfigDto.COLLECTION).insertOne(doc);

                return configDto.getValue();
            }

            return objectMapper.readValue(doc.toJson(), ConfigDto.class).getValue();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize config record", e);
        }
    }

}
