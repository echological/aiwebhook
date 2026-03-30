package org.techsolution.webhook.network.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.http.HttpClient;
import java.time.Duration;

@ApplicationScoped
public class AiConverterAdapter {

    private static final String DEFAULT_BASE_URL = "https://api.openai.com/v1";
    private static final String ENDPOINT = "/responses";
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Inject
    private ObjectMapper objectMapper;
    @ConfigProperty(name = "openai.api-key")
    private String apiKey;
    @ConfigProperty(name = "openai.base-url", defaultValue = DEFAULT_BASE_URL)
    private String baseUrl;

}
