package com.avrist.webhook.factory;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import com.avrist.webhook.config.AppConfig;

@ApplicationScoped
public class MongoDataConnectionFactory {

    @Inject
    MongoClient mongoClient;

    @Inject
    AppConfig appConfig;

    public MongoDatabase database() {
        return mongoClient.getDatabase(appConfig.getMongoDatabase());
    }

    public MongoCollection<Document> collection(String name) {
        return database().getCollection(name);
    }
}