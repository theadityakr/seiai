package com.seiai.server.service;

import com.seiai.server.model.CompletionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AzureOpenAIService {

    private final CompletionModel completionModel;

    public AzureOpenAIService(CompletionModel completionModel) {
        this.completionModel = completionModel;
    }

    public String textCompletion(String prompt) {
        return completionModel.sendRequest(prompt);
    }
}
