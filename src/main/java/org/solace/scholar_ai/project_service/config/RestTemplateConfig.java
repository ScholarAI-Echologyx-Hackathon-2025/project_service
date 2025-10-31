package org.solace.scholar_ai.project_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for RestTemplate beans used for HTTP client operations.
 * Configures timeout settings for connection and read operations.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates a RestTemplate bean with configured timeout settings.
     * Both connection and read timeouts are set to 2 minutes (120000 milliseconds).
     *
     * @return A configured RestTemplate instance with timeout settings
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(120000); // 2 minutes
        factory.setReadTimeout(120000); // 2 minutes

        return new RestTemplate(factory);
    }
}
