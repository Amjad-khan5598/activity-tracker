package com.example.activitytracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    private static final Logger logger =
            LoggerFactory.getLogger(GeminiConfig.class);

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public GeminiClientProvider geminiClientProvider() {
        String apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            logger.warn(
                    "GEMINI_API_KEY is missing. AI Coach will be unavailable."
            );
            return new GeminiClientProvider(null);
        }

        return new GeminiClientProvider(
                Client.builder().apiKey(apiKey).build()
        );
    }

    public record GeminiClientProvider(Client client) {}
}
