package org.techsolution.webhook.factory;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MongoDataConnectionFactory {

    @Inject
    MongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String databaseName;

    public MongoDatabase database() {
        return mongoClient.getDatabase(databaseName);
    }

    public MongoCollection<Document> collection(String name) {
        return database().getCollection(name);
    }
}