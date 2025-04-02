package com.seiai.server.controller;

import com.seiai.server.model.AzureOpenAIResponse;
import com.seiai.server.services.AzureOpenAIService;
import com.seiai.server.services.WidgetProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/azure")
public class AzureOpenAIController {

    @Autowired
    private AzureOpenAIService azureOpenAIService;
    private final WidgetProcessingService widgetProcessingService;

    @Autowired
    public AzureOpenAIController(WidgetProcessingService widgetProcessingService) {
        this.widgetProcessingService = widgetProcessingService;
    }

    @PostMapping("/openai/completions")
    public ResponseEntity<?> completion(@RequestParam String prompt,
                                        @RequestParam(required = false) String image) {
        try {
            AzureOpenAIResponse response = azureOpenAIService.chatCompletion(prompt, Optional.ofNullable(image));
            widgetProcessingService.processAndSaveWidget(response, "38.126.136.103");
            return ResponseEntity.ok(response.getChoices().get(0).getMessage().getContent());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request: " + e.getMessage());
        }
    }

    @PostMapping(value = "/openai/completions/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> completionWithFile(@RequestParam String prompt,
                                                @RequestParam(required = false) MultipartFile imageFile) {
        try {
            Optional<MultipartFile> optionalImageFile = Optional.ofNullable(imageFile);
            AzureOpenAIResponse response = azureOpenAIService.chatCompletionWithFile(prompt, optionalImageFile);

            widgetProcessingService.processAndSaveWidget(response, "38.126.136.103");
            return ResponseEntity.ok(response.getChoices().get(0).getMessage().getContent());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request: " + e.getMessage());
        }
    }
}
