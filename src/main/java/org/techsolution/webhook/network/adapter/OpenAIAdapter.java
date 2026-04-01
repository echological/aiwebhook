package org.techsolution.webhook.network.adapter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.techsolution.webhook.config.AppConfig;

import java.net.http.HttpClient;
import java.time.Duration;

@ApplicationScoped
public class OpenAIAdapter {

    private static final String ENDPOINT = "/responses";
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Inject
    private AppConfig appConfig;

    public String responsesEndpointUrl() {
        return appConfig.getOpenaiBaseUrl() + ENDPOINT;
    }

    public String apiKey() {
        return appConfig.getOpenaiApiKey();
    }

    public HttpClient client() {
        return httpClient;
    }

}
