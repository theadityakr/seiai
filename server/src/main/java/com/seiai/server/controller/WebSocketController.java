package com.seiai.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seiai.server.domain.Widget;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MessageMapping("/widget/update")
    @SendTo("/topic/widget-updates")
    public String handleWidgetUpdate(Widget widget) throws JsonProcessingException {
        return objectMapper.writeValueAsString(widget);
    }

    @MessageMapping("/update")
    @SendTo("/topic/updates")
    public String sendUpdate(String message) {
        return message;
    }
}