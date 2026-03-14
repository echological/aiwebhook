package org.techsolution.webhook.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

/**
 * Centralized app config - inject di mana saja.
 */
@ApplicationScoped
@Getter
public class AppConfig {

    @ConfigProperty(name = "app.name", defaultValue = "AI Webhook")
    String appName;

    @ConfigProperty(name = "app.version", defaultValue = "1.0.0")
    String appVersion;

    @ConfigProperty(name = "app.env", defaultValue = "dev")
    String env;

    public boolean isDev() {
        return "dev".equalsIgnoreCase(env);
    }
}
