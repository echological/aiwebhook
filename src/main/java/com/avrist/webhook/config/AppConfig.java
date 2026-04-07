package com.avrist.webhook.config;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Getter
public class AppConfig {

    @ConfigProperty(name = "app.name", defaultValue = "AI Webhook")
    String appName;

    @ConfigProperty(name = "app.version", defaultValue = "1.0.0")
    String appVersion;

    @ConfigProperty(name = "app.env", defaultValue = "dev")
    private String env;

    @ConfigProperty(name = "quarkus.mongodb.database")
    private String mongoDatabase;

    @ConfigProperty(name = "openai.api-key")
    private String openaiApiKey;

    @ConfigProperty(name = "openai.base-url", defaultValue = "https://api.openai.com/v1")
    private String openaiBaseUrl;

    @ConfigProperty(name = "telegram.bot.token")
    private String telegramBotToken;

    @ConfigProperty(name = "telegram.bot.base-url", defaultValue = "https://api.telegram.org")
    private String telegramBotBaseUrl;

    @ConfigProperty(name = "telegram.webhook.api-key")
    private String telegramWebhookApiKey;

    @ConfigProperty(name = "http-log.mongo.enabled", defaultValue = "true")
    private boolean httpLogMongoEnabled;

    @ConfigProperty(name = "http-log.mongo.collection", defaultValue = "http_request_response_logs")
    private String httpLogMongoCollection;

    @ConfigProperty(name = "error-log.mongo.enabled", defaultValue = "true")
    private boolean errorLogMongoEnabled;

    @ConfigProperty(name = "error-log.mongo.collection", defaultValue = "error_logs")
    private String errorLogMongoCollection;

    public boolean isDev() {
        return "dev".equalsIgnoreCase(env);
    }
}
