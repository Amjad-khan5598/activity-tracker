package com.example.activitytracker.service;

import org.springframework.stereotype.Service;

import com.example.activitytracker.config.GeminiConfig.GeminiClientProvider;
import com.example.activitytracker.exception.AIConfigurationException;
import com.google.genai.Client;

@Service
public class GeminiClientServiceImpl implements GeminiClientService {

    private final Client geminiClient;

    public GeminiClientServiceImpl(GeminiClientProvider provider) {
        this.geminiClient = provider.client();
    }

    @Override
    public String generateContent(String prompt) {

        if (geminiClient == null) {
            throw new AIConfigurationException(
                    "Gemini API key is not configured.",
                    new IllegalStateException("GEMINI_API_KEY is missing")
            );
        }

        return geminiClient.models.generateContent(
                "gemini-3.5-flash-lite",
                prompt,
                null
        ).text();
    }
}
