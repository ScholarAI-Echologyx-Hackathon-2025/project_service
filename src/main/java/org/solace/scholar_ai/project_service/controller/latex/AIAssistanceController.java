package org.solace.scholar_ai.project_service.controller.latex;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.solace.scholar_ai.project_service.dto.response.APIResponse;
import org.solace.scholar_ai.project_service.service.latex.AIAssistanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for AI assistance features for LaTeX documents.
 * Provides endpoints for document review, writing suggestions, compliance
 * checking, citation validation, corrections, chat interactions, and
 * final review generation.
 */
@RestController
@RequestMapping("/api/ai-assistance")
@RequiredArgsConstructor
public class AIAssistanceController {

        private final AIAssistanceService aiAssistanceService;

        /**
         * Reviews a document and provides feedback on content, structure, and quality.
         *
         * @param request Map containing "content" (required)
         * @return ResponseEntity containing review results
         */
        @PostMapping("/review")
        public ResponseEntity<APIResponse<Map<String, Object>>> reviewDocument(
                        @RequestBody Map<String, String> request) {

                String content = request.get("content");
                if (content == null || content.trim().isEmpty()) {
                        return ResponseEntity.badRequest()
                                        .body(APIResponse.<Map<String, Object>>builder()
                                                        .status(400)
                                                        .message("Content is required")
                                                        .build());
                }

                Map<String, Object> review = aiAssistanceService.reviewDocument(content);

                return ResponseEntity.ok(APIResponse.<Map<String, Object>>builder()
                                .status(200)
                                .message("Document review completed")
                                .data(review)
                                .build());
        }

        /**
         * Generates contextual writing suggestions based on content and context.
         *
         * @param request Map containing "content" (required) and "context" (optional)
         * @return ResponseEntity containing writing suggestions
         */
        @PostMapping("/suggestions")
        public ResponseEntity<APIResponse<String>> generateSuggestions(@RequestBody Map<String, String> request) {

                String content = request.get("content");
                String context = request.get("context");

                if (content == null || content.trim().isEmpty()) {
                        return ResponseEntity.badRequest()
                                        .body(APIResponse.<String>builder()
                                                        .status(400)
                                                        .message("Content is required")
                                                        .build());
                }

                String suggestions = aiAssistanceService.generateContextualSuggestions(content, context);

                return ResponseEntity.ok(APIResponse.<String>builder()
                                .status(200)
                                .message("Writing suggestions generated")
                                .data(suggestions)
                                .build());
        }

        /**
         * Checks document compliance with venue-specific requirements.
         *
         * @param request Map containing "content" (required) and "venue" (optional)
         * @return ResponseEntity containing compliance check results
         */
        @PostMapping("/compliance")
        public ResponseEntity<APIResponse<Map<String, Object>>> checkCompliance(
                        @RequestBody Map<String, String> request) {

                String content = request.get("content");
                String venue = request.get("venue");

                if (content == null || content.trim().isEmpty()) {
                        return ResponseEntity.badRequest()
                                        .body(APIResponse.<Map<String, Object>>builder()
                                                        .status(400)
                                                        .message("Content is required")
                                                        .build());
                }

                Map<String, Object> compliance = aiAssistanceService.checkCompliance(content, venue);

                return ResponseEntity.ok(APIResponse.<Map<String, Object>>builder()
                                .status(200)
                                .message("Compliance check completed")
                                .data(compliance)
                                .build());
        }

        /**
         * Validates citations in the document content.
         *
         * @param request Map containing "content" (required)
         * @return ResponseEntity containing citation validation results
         */
        @PostMapping("/citations/validate")
        public ResponseEntity<APIResponse<Map<String, Object>>> validateCitations(
                        @RequestBody Map<String, String> request) {

                String content = request.get("content");

                if (content == null || content.trim().isEmpty()) {
                        return ResponseEntity.badRequest()
                                        .body(APIResponse.<Map<String, Object>>builder()
                                                        .status(400)
                                                        .message("Content is required")
                                                        .build());
                }

                Map<String, Object> validation = aiAssistanceService.validateCitations(content);

                return ResponseEntity.ok(APIResponse.<Map<String, Object>>builder()
                                .status(200)
                                .message("Citation validation completed")
                                .data(validation)
                                .build());
        }

        /**
         * Generates AI-powered corrections for document content.
         *
         * @param request Map containing "content" (required)
         * @return ResponseEntity containing correction suggestions
         */
        @PostMapping("/corrections")
        public ResponseEntity<APIResponse<Map<String, Object>>> generateCorrections(
                        @RequestBody Map<String, String> request) {

                String content = request.get("content");

                if (content == null || content.trim().isEmpty()) {
                        return ResponseEntity.badRequest()
                                        .body(APIResponse.<Map<String, Object>>builder()
                                                        .status(400)
                                                        .message("Content is required")
                                                        .build());
                }

                Map<String, Object> corrections = aiAssistanceService.generateCorrections(content);

                return ResponseEntity.ok(APIResponse.<Map<String, Object>>builder()
                                .status(200)
                                .message("AI corrections generated")
                                .data(corrections)
                                .build());
        }

        /**
         * Processes a chat request with context from selected text and full document.
         *
         * @param request Map containing "userRequest" (required), "selectedText"
         *                (optional), and "fullDocument" (optional)
         * @return ResponseEntity containing the AI's chat response
         */
        @PostMapping("/chat")
        public ResponseEntity<APIResponse<String>> processChatRequest(@RequestBody Map<String, String> request) {

                String selectedText = request.get("selectedText");
                String userRequest = request.get("userRequest");
                String fullDocument = request.get("fullDocument");

                if (userRequest == null || userRequest.trim().isEmpty()) {
                        return ResponseEntity.badRequest()
                                        .body(APIResponse.<String>builder()
                                                        .status(400)
                                                        .message("User request is required")
                                                        .build());
                }

                String aiResponse = aiAssistanceService.processChatRequest(
                                selectedText != null ? selectedText : "", userRequest,
                                fullDocument != null ? fullDocument : "");

                return ResponseEntity.ok(APIResponse.<String>builder()
                                .status(200)
                                .message("AI response generated")
                                .data(aiResponse)
                                .build());
        }

        /**
         * Generates a comprehensive final review of the document.
         *
         * @param request Map containing "content" (required)
         * @return ResponseEntity containing the comprehensive review
         */
        @PostMapping("/final-review")
        public ResponseEntity<APIResponse<String>> generateFinalReview(@RequestBody Map<String, String> request) {

                String content = request.get("content");

                if (content == null || content.trim().isEmpty()) {
                        return ResponseEntity.badRequest()
                                        .body(APIResponse.<String>builder()
                                                        .status(400)
                                                        .message("Content is required")
                                                        .build());
                }

                String finalReview = aiAssistanceService.generateComprehensiveFinalReview(content);

                return ResponseEntity.ok(APIResponse.<String>builder()
                                .status(200)
                                .message("Comprehensive final review generated successfully")
                                .data(finalReview)
                                .build());
        }
}
