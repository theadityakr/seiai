package com.seiai.server.model;

import com.seiai.server.config.AzureOpenAIConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AzureOpenAIModel {
    protected AzureOpenAIConfig config;
    protected double temperature = 0.2;
    protected int maxTokens = 1500;
    protected RestTemplate restTemplate = new RestTemplate();

    public AzureOpenAIModel(AzureOpenAIConfig config) {
        this.config = config;
    }

    public abstract AzureOpenAIResponse sendRequest(String prompt, Optional<String> image);

    public abstract AzureOpenAIResponse sendRequestWithFile(String prompt, Optional<MultipartFile> image);
}
