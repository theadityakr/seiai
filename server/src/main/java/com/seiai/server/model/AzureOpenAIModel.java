package com.seiai.server.model;

import com.seiai.server.config.AzureOpenAIConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Data
public abstract class AzureOpenAIModel {
    protected AzureOpenAIConfig config;
    protected double temperature = 0.7;
    protected int maxTokens = 1200;
    protected RestTemplate restTemplate = new RestTemplate();

    public AzureOpenAIModel(AzureOpenAIConfig config) {
        this.config = config;
    }

    public abstract String sendRequest(String prompt);
}
