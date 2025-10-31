package org.solace.scholar_ai.project_service.messaging.publisher.extraction;

import lombok.extern.slf4j.Slf4j;
import org.solace.scholar_ai.project_service.config.RabbitMQConfig;
import org.solace.scholar_ai.project_service.dto.request.extraction.ExtractorMessageRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for sending extraction requests to the extractor service via RabbitMQ.
 * Publishes extraction job requests for processing PDF content from research papers.
 */
@Slf4j
@Service
public class ExtractionRequestSender {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfig rabbitMQConfig;

    /**
     * Creates a new ExtractionRequestSender with the required dependencies.
     *
     * @param rabbitTemplate  The RabbitTemplate for sending messages
     * @param rabbitMQConfig  The RabbitMQ configuration containing exchange and routing keys
     */
    public ExtractionRequestSender(RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    /**
     * Sends an extraction request to the extractor service via RabbitMQ.
     * Publishes the request to the configured exchange with the extraction routing key.
     *
     * @param request The extraction request message containing paper ID, job ID, and PDF URL
     * @throws RuntimeException if the message cannot be sent
     */
    public void send(ExtractorMessageRequest request) {
        try {
            log.info("Sending extraction request for paper {} with job ID: {}", request.paperId(), request.jobId());

            rabbitTemplate.convertAndSend(
                    rabbitMQConfig.getExchangeName(), rabbitMQConfig.getExtractionRoutingKey(), request);

            log.info("Successfully sent extraction request for job ID: {}", request.jobId());
        } catch (Exception e) {
            log.error(
                    "Failed to send extraction request for job ID: {}, error: {}", request.jobId(), e.getMessage(), e);
            throw new RuntimeException("Failed to send extraction request", e);
        }
    }
}
