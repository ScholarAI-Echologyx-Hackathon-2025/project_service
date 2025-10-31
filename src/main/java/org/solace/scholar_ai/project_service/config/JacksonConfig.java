package org.solace.scholar_ai.project_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Configuration for Jackson ObjectMapper used for JSON serialization and deserialization.
 * Configures custom date/time formatting and timezone settings.
 */
@Configuration
public class JacksonConfig {

    private static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    /**
     * Creates the primary ObjectMapper bean with custom date/time formatting.
     * Configures LocalDateTime serialization/deserialization using a custom format
     * and sets the timezone to UTC. Disables writing dates as timestamps.
     *
     * @return The configured ObjectMapper instance
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        return Jackson2ObjectMapperBuilder.json()
                .modules(javaTimeModule)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .timeZone(java.util.TimeZone.getTimeZone("UTC"))
                .build();
    }
}
