package com.seiai.server.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.http.HttpHeaders;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "azure.openai")
public class AzureOpenAIConfig {
    private String apiKey;
    private String endpoint;
    private String apiVersion;

    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);

        headers.set("Content-Type", "application/json");
        return headers;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getApiVersion() {
        return apiVersion;
    }
}
