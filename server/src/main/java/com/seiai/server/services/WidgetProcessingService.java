package com.seiai.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seiai.server.domain.Widget;
import com.seiai.server.model.AzureOpenAIResponse;
import com.seiai.server.repositories.WidgetRepository;
import com.seiai.server.services.database.WidgetUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

@Service
public class WidgetProcessingService {

    @Autowired
    private WidgetUpdateService widgetUpdateService;

    @Autowired
    private OverlayService overlayService;

    private final WidgetRepository widgetRepository;
    private final ObjectMapper objectMapper;

    public WidgetProcessingService(WidgetRepository widgetRepository, ObjectMapper objectMapper) {
        this.widgetRepository = widgetRepository;
        this.objectMapper = objectMapper;
    }

    private String getStringValue(Object obj) {
        return obj != null ? obj.toString() : null;
    }

    private String convertObjectToJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to convert object to JSON: " + e.getMessage());
            return obj != null ? obj.toString() : null;
        }
    }

    private Map<String, Object> parseResponseMessage(String responseMessage) throws JsonProcessingException {
        return objectMapper.readValue(responseMessage, Map.class);
    }

    public void processAndSaveWidget(AzureOpenAIResponse azureOpenAIResponse, String ipAddress) {
        String responseMessage = azureOpenAIResponse.getChoices().get(0).getMessage().getContent();
        Widget.WidgetBuilder widgetBuilder = Widget.builder().ipAddress(ipAddress);

        try {
            Map<String, Object> parsedResponse = parseResponseMessage(responseMessage);
            widgetBuilder.explanation(getStringValue(parsedResponse.get("explanation")));
            widgetBuilder.code(getStringValue(parsedResponse.get("code")));
            Object extraObj = parsedResponse.get("extra");
            if (extraObj instanceof Map) {
                widgetBuilder.extra(convertObjectToJsonString(extraObj));
            } else {
                widgetBuilder.extra(getStringValue(extraObj));
            }

        } catch (JsonProcessingException e) {
            widgetBuilder.explanation(responseMessage);
            System.err.println("Failed to parse response: " + e.getMessage());
        }

        try {
            Widget widget = widgetBuilder.build();
            widgetRepository.save(widget);
            widgetUpdateService.sendWidgetUpdate(ipAddress);
            overlayService.updateWidget(ipAddress);

        } catch (Exception e) {
            System.err.println("Failed to save/send widget update: " + e.getMessage());
        }
    }

}