package org.solace.scholar_ai.project_service.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application.
 * Provides centralized exception handling for REST controllers,
 * converting exceptions into appropriate HTTP responses with error details.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles PaperNotExtractedException and returns a 400 Bad Request response.
     * This exception occurs when attempting to perform operations on a paper
     * that has not yet been extracted.
     *
     * @param ex The PaperNotExtractedException that was thrown
     * @return ResponseEntity containing error details including timestamp, status,
     *         error message, and error code
     */
    @ExceptionHandler(PaperNotExtractedException.class)
    public ResponseEntity<Map<String, Object>> handlePaperNotExtractedException(PaperNotExtractedException ex) {
        log.warn("Paper not extracted exception: {}", ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("code", "PAPER_NOT_EXTRACTED");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles general RuntimeException and returns a 500 Internal Server Error response.
     * This is a catch-all handler for any unexpected runtime exceptions that occur
     * during request processing.
     *
     * @param ex The RuntimeException that was thrown
     * @return ResponseEntity containing error details including timestamp, status,
     *         error message, and error code
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred: {}", ex.getMessage(), ex);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "An unexpected error occurred");
        errorResponse.put("code", "INTERNAL_ERROR");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
