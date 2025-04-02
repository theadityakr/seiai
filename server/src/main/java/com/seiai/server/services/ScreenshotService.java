package com.seiai.server.services;

import com.seiai.server.model.AzureOpenAIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ScreenshotService {

    private final WidgetProcessingService widgetProcessingService;
    @Autowired
    private AzureOpenAIService azureOpenAIService;

    @Autowired
    public ScreenshotService(WidgetProcessingService widgetProcessingService) {
        this.widgetProcessingService = widgetProcessingService;
    }

    public void uploadScreenshot(MultipartFile screenshot) {
        String prompt = "";
        AzureOpenAIResponse response = azureOpenAIService.chatCompletionWithFile(prompt, Optional.ofNullable(screenshot));
        System.out.println(response.getChoices().get(0).getMessage().getContent());
        widgetProcessingService.processAndSaveWidget(response,"38.126.136.103");

    }
}
