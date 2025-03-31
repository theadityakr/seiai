package com.seiai.server.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ScreenshotService {

    private final RestTemplate restTemplate;
    private final String apiUrl = "https://your-api-endpoint.com/upload"; // Replace with your API URL

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void uploadScreenshot(byte[] screenshot) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(screenshot) {
            @Override
            public String getFilename() {
                return "screenshot.png";
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                apiUrl,
                requestEntity,
                String.class
        );

        System.out.println("Upload response: " + response.getStatusCode());
    }
}
