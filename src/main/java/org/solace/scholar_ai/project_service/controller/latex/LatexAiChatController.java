package org.solace.scholar_ai.project_service.controller.latex;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.solace.scholar_ai.project_service.dto.latex.*;
import org.solace.scholar_ai.project_service.dto.response.APIResponse;
import org.solace.scholar_ai.project_service.service.latex.LatexAiChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for LaTeX AI chat functionality.
 * Provides endpoints for managing chat sessions, sending messages, applying
 * suggestions, and managing document checkpoints for LaTeX documents.
 */
@RestController
@RequestMapping("/api/latex-ai-chat")
@RequiredArgsConstructor
@Slf4j
public class LatexAiChatController {

    private final LatexAiChatService latexAiChatService;

    /**
     * Gets or creates a chat session for a document.
     *
     * @param documentId The UUID of the document
     * @param projectId  The UUID of the project
     * @return ResponseEntity containing the chat session
     */
    @GetMapping("/session/{documentId}")
    public ResponseEntity<APIResponse<LatexAiChatSessionDto>> getChatSession(
            @PathVariable UUID documentId, @RequestParam UUID projectId) {

        log.info("Getting chat session for document: {}, project: {}", documentId, projectId);

        try {
            LatexAiChatSessionDto session = latexAiChatService.getOrCreateChatSession(documentId, projectId);

            return ResponseEntity.ok(APIResponse.<LatexAiChatSessionDto>builder()
                    .status(200)
                    .message("Chat session retrieved successfully")
                    .data(session)
                    .build());
        } catch (Exception e) {
            log.error("Error getting chat session", e);
            return ResponseEntity.internalServerError()
                    .body(APIResponse.<LatexAiChatSessionDto>builder()
                            .status(500)
                            .message("Failed to get chat session: " + e.getMessage())
                            .build());
        }
    }

    /**
     * Sends a message to the AI chat for a document and receives an AI response.
     *
     * @param documentId The UUID of the document
     * @param request    The chat message request
     * @return ResponseEntity containing the AI's response message
     */
    @PostMapping("/session/{documentId}/message")
    public ResponseEntity<APIResponse<LatexAiChatMessageDto>> sendMessage(
            @PathVariable UUID documentId, @RequestBody CreateLatexChatMessageRequest request) {

        log.info("Sending message to chat for document: {}", documentId);

        try {
            LatexAiChatMessageDto aiMessage = latexAiChatService.sendMessage(documentId, request);

            return ResponseEntity.ok(APIResponse.<LatexAiChatMessageDto>builder()
                    .status(200)
                    .message("Message processed successfully")
                    .data(aiMessage)
                    .build());
        } catch (Exception e) {
            log.error("Error processing message", e);
            return ResponseEntity.internalServerError()
                    .body(APIResponse.<LatexAiChatMessageDto>builder()
                            .status(500)
                            .message("Failed to process message: " + e.getMessage())
                            .build());
        }
    }

    /**
     * Retrieves the chat history for a document.
     *
     * @param documentId The UUID of the document
     * @return ResponseEntity containing a list of chat messages
     */
    @GetMapping("/session/{documentId}/messages")
    public ResponseEntity<APIResponse<List<LatexAiChatMessageDto>>> getChatHistory(@PathVariable UUID documentId) {

        log.info("Getting chat history for document: {}", documentId);

        try {
            List<LatexAiChatMessageDto> messages = latexAiChatService.getChatHistory(documentId);

            return ResponseEntity.ok(APIResponse.<List<LatexAiChatMessageDto>>builder()
                    .status(200)
                    .message("Chat history retrieved successfully")
                    .data(messages)
                    .build());
        } catch (Exception e) {
            log.error("Error getting chat history", e);
            return ResponseEntity.internalServerError()
                    .body(APIResponse.<List<LatexAiChatMessageDto>>builder()
                            .status(500)
                            .message("Failed to get chat history: " + e.getMessage())
                            .build());
        }
    }

    /**
     * Marks a suggestion as applied and stores the content after the change.
     *
     * @param messageId    The ID of the message containing the suggestion
     * @param contentAfter The document content after applying the suggestion
     * @return ResponseEntity confirming the suggestion was applied
     */
    @PostMapping("/message/{messageId}/apply")
    public ResponseEntity<APIResponse<String>> applySuggestion(
            @PathVariable Long messageId, @RequestBody String contentAfter) {

        log.info("Applying suggestion for message: {}", messageId);

        try {
            latexAiChatService.markSuggestionAsApplied(messageId, contentAfter);

            return ResponseEntity.ok(APIResponse.<String>builder()
                    .status(200)
                    .message("Suggestion applied successfully")
                    .data("Applied")
                    .build());
        } catch (Exception e) {
            log.error("Error applying suggestion", e);
            return ResponseEntity.internalServerError()
                    .body(APIResponse.<String>builder()
                            .status(500)
                            .message("Failed to apply suggestion: " + e.getMessage())
                            .build());
        }
    }

    /**
     * Creates a checkpoint for a document to enable restoration to a previous
     * state.
     *
     * @param documentId The UUID of the document
     * @param sessionId  The UUID of the chat session
     * @param request    The checkpoint creation request with message ID and content
     * @return ResponseEntity containing the created checkpoint
     */
    @PostMapping("/document/{documentId}/checkpoint")
    public ResponseEntity<APIResponse<LatexDocumentCheckpointDto>> createCheckpoint(
            @PathVariable UUID documentId, @RequestParam UUID sessionId, @RequestBody CreateCheckpointRequest request) {

        log.info("Creating checkpoint for document: {}", documentId);

        try {
            LatexDocumentCheckpointDto checkpoint = latexAiChatService.createCheckpoint(
                    documentId,
                    sessionId,
                    request.getMessageId(),
                    request.getCheckpointName(),
                    request.getContentBefore(),
                    request.getContentAfter());

            return ResponseEntity.ok(APIResponse.<LatexDocumentCheckpointDto>builder()
                    .status(200)
                    .message("Checkpoint created successfully")
                    .data(checkpoint)
                    .build());
        } catch (Exception e) {
            log.error("Error creating checkpoint", e);
            return ResponseEntity.internalServerError()
                    .body(APIResponse.<LatexDocumentCheckpointDto>builder()
                            .status(500)
                            .message("Failed to create checkpoint: " + e.getMessage())
                            .build());
        }
    }

    /**
     * Restores a document to a specific checkpoint state.
     *
     * @param checkpointId The ID of the checkpoint to restore to
     * @return ResponseEntity containing the restored document content
     */
    @PostMapping("/checkpoint/{checkpointId}/restore")
    public ResponseEntity<APIResponse<String>> restoreToCheckpoint(@PathVariable Long checkpointId) {

        log.info("Restoring to checkpoint: {}", checkpointId);

        try {
            String restoredContent = latexAiChatService.restoreToCheckpoint(checkpointId);

            return ResponseEntity.ok(APIResponse.<String>builder()
                    .status(200)
                    .message("Document restored successfully")
                    .data(restoredContent)
                    .build());
        } catch (Exception e) {
            log.error("Error restoring checkpoint", e);
            return ResponseEntity.internalServerError()
                    .body(APIResponse.<String>builder()
                            .status(500)
                            .message("Failed to restore checkpoint: " + e.getMessage())
                            .build());
        }
    }

    /**
     * Retrieves all checkpoints for a document.
     *
     * @param documentId The UUID of the document
     * @return ResponseEntity containing a list of checkpoints
     */
    @GetMapping("/document/{documentId}/checkpoints")
    public ResponseEntity<APIResponse<List<LatexDocumentCheckpointDto>>> getCheckpoints(@PathVariable UUID documentId) {

        log.info("Getting checkpoints for document: {}", documentId);

        try {
            List<LatexDocumentCheckpointDto> checkpoints = latexAiChatService.getCheckpoints(documentId);

            return ResponseEntity.ok(APIResponse.<List<LatexDocumentCheckpointDto>>builder()
                    .status(200)
                    .message("Checkpoints retrieved successfully")
                    .data(checkpoints)
                    .build());
        } catch (Exception e) {
            log.error("Error getting checkpoints", e);
            return ResponseEntity.internalServerError()
                    .body(APIResponse.<List<LatexDocumentCheckpointDto>>builder()
                            .status(500)
                            .message("Failed to get checkpoints: " + e.getMessage())
                            .build());
        }
    }
}
