package com.seiai.server.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUpdateService {

    private final SimpMessagingTemplate messagingTemplate;

    public DatabaseUpdateService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyClients(String newData) {
        messagingTemplate.convertAndSend("/topic/data", newData);
    }
}