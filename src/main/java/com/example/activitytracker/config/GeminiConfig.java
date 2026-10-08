package com.example.activitytracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.genai.Client;

@Configuration
public class GeminiConfig {

    @Bean
    public Client geminiClient() {

        String apiKey = System.getenv("GEMINI_API_KEY");

        return Client.builder()
                .apiKey(apiKey)
                .build();
    }
}