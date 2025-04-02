package com.seiai.server.services.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUpdateService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendDatabaseUpdateNotification() {
        // This will be sent to all subscribed clients
        messagingTemplate.convertAndSend("/topic/updates", "Database updated at " + System.currentTimeMillis());

        // Or send actual updated data
        // messagingTemplate.convertAndSend("/topic/updates", yourUpdatedData);
    }
}