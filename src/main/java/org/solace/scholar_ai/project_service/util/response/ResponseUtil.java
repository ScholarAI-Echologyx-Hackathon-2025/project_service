package org.solace.scholar_ai.project_service.util.response;

import java.time.LocalDateTime;
import org.solace.scholar_ai.project_service.dto.response.APIErrorResponse;
import org.solace.scholar_ai.project_service.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for creating standardized API responses.
 */
public class ResponseUtil {

    private ResponseUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates an APIErrorResponse with the given parameters.
     * Includes timestamp, HTTP status, error code, message, and suggestion.
     *
     * @param status    The HTTP status code
     * @param errorCode The error code enum value
     * @param message   The error message
     * @return A configured APIErrorResponse instance
     */
    public static APIErrorResponse createErrorResponse(HttpStatus status, ErrorCode errorCode, String message) {
        return APIErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .code(errorCode.name())
                .message(message)
                .suggestion(errorCode.getSuggestion())
                .build();
    }

    /**
     * Creates a success response with data and HTTP 200 OK status.
     *
     * @param <T>  The type of data in the response
     * @param data The data to include in the success response
     * @return A ResponseEntity with HTTP 200 OK containing the wrapped data
     */
    public static <T> ResponseEntity<ResponseWrapper<T>> success(T data) {
        return ResponseEntity.ok(ResponseWrapper.success(data));
    }

    /**
     * Creates a success response with data and a custom HTTP status code.
     * Useful for responses like 201 Created, 202 Accepted, etc.
     *
     * @param <T>    The type of data in the response
     * @param data   The data to include in the success response
     * @param status The HTTP status code to use
     * @return A ResponseEntity with the specified status containing the wrapped
     *         data
     */
    public static <T> ResponseEntity<ResponseWrapper<T>> success(T data, HttpStatus status) {
        return new ResponseEntity<>(ResponseWrapper.success(data), status);
    }

    /**
     * Creates an error response with the specified HTTP status and error details.
     *
     * @param <T>       The type parameter (usually unused for error responses)
     * @param status    The HTTP status code for the error
     * @param errorCode The error code enum value
     * @param message   The error message
     * @return A ResponseEntity with the specified status containing the error
     *         response
     */
    public static <T> ResponseEntity<ResponseWrapper<T>> error(HttpStatus status, ErrorCode errorCode, String message) {
        return ResponseEntity.status(status)
                .body(ResponseWrapper.error(createErrorResponse(status, errorCode, message)));
    }
}
