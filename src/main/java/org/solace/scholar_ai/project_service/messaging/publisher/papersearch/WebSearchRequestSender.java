package org.solace.scholar_ai.project_service.messaging.publisher.papersearch;

import org.solace.scholar_ai.project_service.config.RabbitMQConfig;
import org.solace.scholar_ai.project_service.dto.request.papersearch.WebSearchRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for sending web search requests to the paper search service via RabbitMQ.
 * Publishes academic paper search requests for processing across multiple sources.
 */
@Service
public class WebSearchRequestSender {
    private final RabbitTemplate rt;
    private final RabbitMQConfig rabbitMQConfig;

    /**
     * Creates a new WebSearchRequestSender with the required dependencies.
     *
     * @param rabbitTemplate  The RabbitTemplate for sending messages
     * @param rabbitMQConfig  The RabbitMQ configuration containing exchange and routing keys
     */
    public WebSearchRequestSender(RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig) {
        this.rt = rabbitTemplate;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    /**
     * Sends a web search request to the paper search service via RabbitMQ.
     * Publishes the request to the configured exchange with the web search routing key.
     *
     * @param req The web search request containing query terms, domain, and batch size
     */
    public void send(WebSearchRequest req) {
        rt.convertAndSend(rabbitMQConfig.getExchangeName(), rabbitMQConfig.getWebSearchRoutingKey(), req);
    }
}
