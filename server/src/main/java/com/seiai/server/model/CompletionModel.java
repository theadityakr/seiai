package com.seiai.server.model;

import com.seiai.server.config.AzureOpenAIConfig;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Data
@Component
@ConfigurationProperties(prefix = "azure.openai.model.completion")
public class CompletionModel extends  AzureOpenAIModel{
    private String deployment;

    private String completionUrl;
    private final AzureOpenAIConfig config;

    private final String systemMessage ;

    @Autowired
    public CompletionModel(AzureOpenAIConfig config) {
        super(config);
        this.config = config;
        this.systemMessage = """
                    You are an AI coding assistant that provides structured solutions to programming problems.
                    Always respond with a VALID JSON object containing these EXACT fields:
                    {
                        "explanation": "string (detailed solution approach)",
                        "code": "string (complete implementation)",
                        "extra": {
                            "time_complexity": "string (Big-O notation)",
                            "space_complexity": "string (Big-O notation)",
                            "suggestions": ["array", "of", "optimization", "tips"],
                            "edge_cases": ["array", "of", "important", "edge", "cases"]
                        }
                    }
                
                    RULES:
                    1. Use ONLY double quotes for JSON properties and strings
                    2. Escape all special characters in code (\\n, \\t, \\", etc.)
                    3. Never include markdown syntax (```) or external comments
                    4. For code:
                       - C++ for coding/machine problems
                       - React+TypeScript for frontend
                       - Java Spring for backend
                    5. Keep code self-contained
                    6. Include all required imports in code blocks
                    7. The JSON must parse successfully every time
                
                    Example of GOOD response:
                    {
                        "explanation": "Two Sum problem solution...",
                        "code": "#include <vector>\\n#include <unordered_map>\\n\\nvector<int> twoSum(...) {...}",
                        "extra": {
                            "time_complexity": "O(n)",
                            "space_complexity": "O(n)",
                            "suggestions": ["Use hash map for O(1) lookups"],
                            "edge_cases": ["Empty input", "No solution"]
                        }
                    }
                """;
    }

    @PostConstruct
    public void init() {
        if (deployment == null) {
            throw new IllegalStateException("Deployment name is null! Check application.properties.");
        }
        this.completionUrl = config.getEndpoint()
                + "/openai/deployments/"
                + this.deployment
                + "/chat/completions?api-version="
                + config.getApiVersion();
    }

    private HttpEntity<HashMap<String, Object>> getHashMapHttpEntity(String prompt, AzureOpenAIConfig config, Optional<String> base64image) {
        HttpHeaders headers = config.getHeaders();

        HashMap<String, Object> payload = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>();

        messages.add(Map.of("role", "system", "content", this.systemMessage));
        messages.add(Map.of("role", "user", "content", prompt));

        base64image.ifPresent(image -> messages.add(Map.of("role", "user", "content", "[IMAGE]", "image", image)));

        payload.put("messages", messages);
        payload.put("max_tokens", getMaxTokens());
        payload.put("temperature", getTemperature());

        return new HttpEntity<>(payload, headers);
    }

    private HttpEntity<HashMap<String, Object>> getHashMapHttpEntityWithFile(String prompt, AzureOpenAIConfig config, Optional<MultipartFile> imageFile) throws IOException {

        HttpHeaders headers = config.getHeaders();
        HashMap<String, Object> payload = new HashMap<>();
        List<Map<String, Object>> messages = new ArrayList<>();

        messages.add(Map.of("role", "system", "content", this.systemMessage));
        messages.add(Map.of("role", "user", "content", prompt));

        if (imageFile.isPresent() && !imageFile.get().isEmpty()) {
            MultipartFile file = imageFile.get();
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            String mimeType = file.getContentType();

            List<Map<String, Object>> contentList = new ArrayList<>();
            contentList.add(Map.of("type", "text", "text", "[IMAGE]"));

            Map<String, String> imageUrlMap = new HashMap<>();
            imageUrlMap.put("url", "data:" + mimeType + ";base64," + base64Image);

            Map<String, Object> imageContent = new HashMap<>();
            imageContent.put("type", "image_url");
            imageContent.put("image_url", imageUrlMap);
            contentList.add(imageContent);

            Map<String, Object> imageMessage = new HashMap<>();
            imageMessage.put("role", "user");
            imageMessage.put("content", contentList);

            messages.add(imageMessage);
        }

        payload.put("messages", messages);
        payload.put("max_tokens", getMaxTokens());
        payload.put("temperature", getTemperature());

        return new HttpEntity<>(payload, headers);
    }

    @Override
    public AzureOpenAIResponse sendRequest(String prompt, Optional<String> image) {
        HttpEntity<HashMap<String, Object>> request = getHashMapHttpEntity(prompt, config, image);
        ResponseEntity<AzureOpenAIResponse> response = restTemplate.postForEntity(this.completionUrl, request, AzureOpenAIResponse.class);
        return response.getBody();
    }

    @Override
    public AzureOpenAIResponse sendRequestWithFile(String prompt, Optional<MultipartFile> image) {
        try {
            HttpEntity<HashMap<String, Object>> request = getHashMapHttpEntityWithFile(prompt, config, image);
            ResponseEntity<AzureOpenAIResponse> response = restTemplate.postForEntity(
                    this.completionUrl, request, AzureOpenAIResponse.class);
            return response.getBody();
        } catch (IOException e) {
            throw new RuntimeException("Failed to process image file", e);
        }
    }

}
