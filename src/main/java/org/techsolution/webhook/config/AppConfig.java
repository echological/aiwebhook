package org.techsolution.webhook.config;

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

    public boolean isDev() {
        return "dev".equalsIgnoreCase(env);
    }
}
