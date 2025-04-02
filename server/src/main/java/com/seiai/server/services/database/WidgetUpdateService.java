package com.seiai.server.services.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seiai.server.domain.Widget;
import com.seiai.server.repositories.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WidgetUpdateService {

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendWidgetUpdate(String ipAddress) throws JsonProcessingException {
        widgetRepository.findById(ipAddress).ifPresent(widget -> {
            try {
                String widgetJson = objectMapper.writeValueAsString(widget);
                messagingTemplate.convertAndSend("/topic/widget-updates", widgetJson);
            } catch (JsonProcessingException e) {
                // Handle serialization error
                e.printStackTrace();
            }
        });
    }

    public void sendWidgetUpdate(Widget widget) throws JsonProcessingException {
        String widgetJson = objectMapper.writeValueAsString(widget);
        messagingTemplate.convertAndSend("/topic/widget-updates", widgetJson);
    }
}