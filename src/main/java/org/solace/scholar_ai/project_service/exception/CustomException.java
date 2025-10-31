package org.solace.scholar_ai.project_service.exception;

import java.util.Objects;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base exception class for custom application exceptions.
 * Provides a structured way to handle application-specific errors with
 * associated HTTP status codes and error codes for better error reporting.
 */
@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final ErrorCode errorCode;

    /**
     * Creates a new CustomException with a message and HTTP status.
     * Uses the default INTERNAL_ERROR error code.
     *
     * @param message The error message (must not be null)
     * @param status  The HTTP status code to return (must not be null)
     * @throws NullPointerException if message or status is null
     */
    public CustomException(String message, HttpStatus status) {
        super(Objects.requireNonNull(message, "message must not be null"));
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.errorCode = ErrorCode.INTERNAL_ERROR;
    }

    /**
     * Creates a new CustomException with a message, HTTP status, and error code.
     *
     * @param message   The error message (must not be null)
     * @param status    The HTTP status code to return (must not be null)
     * @param errorCode The specific error code for this exception (must not be null)
     * @throws NullPointerException if message, status, or errorCode is null
     */
    public CustomException(String message, HttpStatus status, ErrorCode errorCode) {
        super(Objects.requireNonNull(message, "message must not be null"));
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.errorCode = Objects.requireNonNull(errorCode, "errorCode must not be null");
    }
}
