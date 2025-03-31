package com.seiai.server.model;

import com.seiai.server.config.AzureOpenAIConfig;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Component
@ConfigurationProperties(prefix = "azure.openai.model.completion")
public class CompletionModel extends  AzureOpenAIModel{
    private String deployment;
    private String completionUrl;

    public CompletionModel(AzureOpenAIConfig config) {
        super(config);
    }

    @PostConstruct
    public void init() {
        this.completionUrl = config.getEndpoint()
                + "/openai/deployments/"
                + this.deployment
                + "/completions?api-version="
                + config.getApiVersion();
    }

    @Override
    public String sendRequest(String prompt){
        HttpHeaders headers = config.getHeaders();

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("prompt",prompt);
        payload.put("temperature", temperature);
        payload.put("max_tokens", maxTokens);

        HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(this.completionUrl, request, String.class);

        return response.getBody();

    }

}
