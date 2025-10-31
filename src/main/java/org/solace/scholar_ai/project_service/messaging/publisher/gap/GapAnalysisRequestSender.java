package org.solace.scholar_ai.project_service.messaging.publisher.gap;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.solace.scholar_ai.project_service.dto.messaging.gap.GapAnalysisMessageRequest;
import org.solace.scholar_ai.project_service.dto.request.gap.GapAnalysisRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Publisher for sending gap analysis requests to RabbitMQ.
 * Publishes gap analysis job requests to the gap analyzer service for processing.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GapAnalysisRequestSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    /** RabbitMQ exchange name for gap analysis requests. */
    @Value("${scholarai.rabbitmq.exchange}")
    private String exchangeName;

    /** RabbitMQ routing key for gap analysis requests. */
    @Value("${scholarai.rabbitmq.gap-analysis.request-routing-key:gap.analysis.request}")
    private String requestRoutingKey;

    /**
     * Sends a gap analysis request to the gap analyzer service via RabbitMQ.
     * Converts the request to a message format and publishes it to the configured exchange.
     *
     * @param request           The gap analysis request containing paper ID and configuration
     * @param paperExtractionId The UUID of the paper extraction to analyze
     * @param correlationId     The correlation ID for tracking the analysis request
     * @param requestId         The unique request ID for this gap analysis
     * @throws RuntimeException if the message cannot be sent
     */
    public void sendGapAnalysisRequest(
            GapAnalysisRequest request, UUID paperExtractionId, String correlationId, String requestId) {
        try {
            log.info("Sending gap analysis request for paper: {}, requestId: {}", request.getPaperId(), requestId);

            // Convert to message format
            GapAnalysisMessageRequest messageRequest = GapAnalysisMessageRequest.builder()
                    .paperId(request.getPaperId())
                    .paperExtractionId(paperExtractionId)
                    .correlationId(correlationId)
                    .requestId(requestId)
                    .config(request.getConfig())
                    .build();

            // Send message to exchange with routing key
            rabbitTemplate.convertAndSend(exchangeName, requestRoutingKey, messageRequest);

            log.info(
                    "Gap analysis request sent successfully for paper: {}, requestId: {}",
                    request.getPaperId(),
                    requestId);

        } catch (Exception e) {
            log.error(
                    "Failed to send gap analysis request for paper: {}, requestId: {}",
                    request.getPaperId(),
                    requestId,
                    e);
            throw new RuntimeException("Failed to send gap analysis request", e);
        }
    }
}
