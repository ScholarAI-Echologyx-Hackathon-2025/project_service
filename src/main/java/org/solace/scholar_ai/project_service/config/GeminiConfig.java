package org.solace.scholar_ai.project_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the Gemini AI API integration.
 * Loads API key and base URL from application properties with prefix "gemini".
 */
@Configuration
@ConfigurationProperties(prefix = "gemini")
@Data
public class GeminiConfig {
    private String apiKey;
    private String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
}
