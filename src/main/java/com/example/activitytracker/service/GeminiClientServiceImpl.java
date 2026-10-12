package com.example.activitytracker.service;
import org.springframework.stereotype.Service;

import com.google.genai.Client;
import com.google.genai.errors.ApiException;

@Service
public class GeminiClientServiceImpl implements GeminiClientService{
	private final Client geminiClient;

    public GeminiClientServiceImpl(Client geminiClient) {
        this.geminiClient = geminiClient;
    }

    @Override
    public String generateContent(String prompt) {
        return geminiClient.models.generateContent(
                "gemini-3.5-flash-lite",
                prompt,
                null
        ).text();
    }

}
