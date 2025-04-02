package com.seiai.server.services;

import com.seiai.server.model.AzureOpenAIResponse;
import com.seiai.server.model.CompletionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class AzureOpenAIService {

    @Autowired
    private CompletionModel completionModel;

    public AzureOpenAIResponse chatCompletion(String prompt, Optional<String> image) {
        return completionModel.sendRequest(prompt, image);
    }

    public AzureOpenAIResponse chatCompletionWithFile(String prompt,Optional<MultipartFile> image) {
        return completionModel.sendRequestWithFile(prompt, image);
    }

}
