package com.seiai.server.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/update")  // Client sends to "/app/update"
    @SendTo("/topic/data")      // Server sends to "/topic/data"
    public String sendUpdate(String message) {
        return message; // Send message to all connected clients
    }
}