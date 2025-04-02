package com.seiai.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seiai.server.domain.Widget;
import com.seiai.server.model.AzureOpenAIResponse;
import com.seiai.server.repositories.WidgetRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WidgetProcessingService {

    private final WidgetRepository widgetRepository;
    private final ObjectMapper objectMapper;

    public WidgetProcessingService(WidgetRepository widgetRepository, ObjectMapper objectMapper) {
        this.widgetRepository = widgetRepository;
        this.objectMapper = objectMapper;
    }

    public void processAndSaveWidget(AzureOpenAIResponse azureOpenAIResponse, String ipAddress) {
        String responseMessage = azureOpenAIResponse.getChoices().get(0).getMessage().getContent();

        try {
            Map<String, Object> parsedResponse = parseResponseMessage(responseMessage);

            Widget widget = Widget.builder()
                    .ipAddress(ipAddress)
                    .explanation((String) parsedResponse.get("explanation"))
                    .code((String) parsedResponse.get("code"))
                    .extra((String) parsedResponse.get("extra"))
                    .build();
            widgetRepository.save(widget);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to parse response message: " + e.getMessage());

        }
    }

    private Map<String, Object> parseResponseMessage(String responseMessage) throws JsonProcessingException {
        return objectMapper.readValue(responseMessage, Map.class);
    }
}