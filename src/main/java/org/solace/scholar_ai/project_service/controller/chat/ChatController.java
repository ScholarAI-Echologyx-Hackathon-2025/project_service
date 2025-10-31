package org.solace.scholar_ai.project_service.controller.chat;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.solace.scholar_ai.project_service.dto.chat.ChatRequest;
import org.solace.scholar_ai.project_service.dto.chat.ChatResponse;
import org.solace.scholar_ai.project_service.service.ai.CommandExecutorService;
import org.solace.scholar_ai.project_service.service.ai.CommandParserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling chat messages with natural language command
 * processing.
 * Processes user messages, parses commands, executes them, and returns
 * appropriate responses.
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final CommandParserService parserService;
    private final CommandExecutorService executorService;

    /**
     * Processes a chat message by parsing the command and executing it.
     * Returns a response with the execution results or an error message if
     * processing fails.
     *
     * @param request The chat request containing the message and user ID
     * @return ResponseEntity containing the chat response with execution results
     */
    @PostMapping("/message")
    public ResponseEntity<ChatResponse> processMessage(@RequestBody ChatRequest request) {
        try {
            log.info("Processing chat message: {}", request.getMessage());

            var parsedCommand = parserService.parseCommand(request.getMessage());

            Map<String, Object> executionResult = executorService.executeCommand(parsedCommand, request.getUserId());

            String message = (String) executionResult.getOrDefault("naturalResponse", "Command processed");
            if (message == null) {
                message = "Command processed successfully";
            }

            ChatResponse response = ChatResponse.builder()
                    .message(message)
                    .data(executionResult)
                    .commandType(parsedCommand.getCommandType().toString())
                    .timestamp(LocalDateTime.now().toString())
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing chat message: {}", e.getMessage(), e);

            ChatResponse errorResponse = ChatResponse.builder()
                    .message("I encountered an error processing your request. Please try again.")
                    .data(Map.of("error", e.getMessage()))
                    .commandType("ERROR")
                    .timestamp(LocalDateTime.now().toString())
                    .build();

            return ResponseEntity.ok(errorResponse);
        }
    }

    /**
     * Health check endpoint for the chat service.
     *
     * @return ResponseEntity with service health status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "ScholarBot"));
    }
}
