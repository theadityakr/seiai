package com.seiai.server.controller;

import com.seiai.server.service.AzureOpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/openai")
public class AzureOpenAIController {

    @Autowired
    private AzureOpenAIService azureOpenAIService;

    @PostMapping("/completion")
    public ResponseEntity<?> completion(@RequestParam String prompt) {
        try {
            String response = azureOpenAIService.textCompletion(prompt);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request: " + e.getMessage());
        }
    }
}
