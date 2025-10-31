package org.solace.scholar_ai.project_service.util.response;

import lombok.Getter;
import org.solace.scholar_ai.project_service.dto.response.APIErrorResponse;

/**
 * Wrapper class for API responses that can handle both success and error cases.
 * Provides a consistent response structure across the API with success flag,
 * data payload, or error information.
 *
 * @param <T> The type of data included in successful responses
 */
@Getter
public class ResponseWrapper<T> {
    /** Indicates whether the response represents a success or error. */
    private final boolean success;
    /** The data payload for successful responses. Null for error responses. */
    private final T data;
    /** The error information for failed responses. Null for successful responses. */
    private final APIErrorResponse error;

    /**
     * Creates a new ResponseWrapper instance.
     *
     * @param success Whether this is a successful response
     * @param data   The data payload (null for errors)
     * @param error  The error information (null for successes)
     */
    private ResponseWrapper(boolean success, T data, APIErrorResponse error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    /**
     * Creates a successful response wrapper with data.
     *
     * @param <T>  The type of data
     * @param data The data to include in the response
     * @return A ResponseWrapper with success=true and the provided data
     */
    public static <T> ResponseWrapper<T> success(T data) {
        return new ResponseWrapper<>(true, data, null);
    }

    /**
     * Creates an error response wrapper with error information.
     *
     * @param <T>   The type parameter (unused for error responses)
     * @param error The error response containing error details
     * @return A ResponseWrapper with success=false and the provided error
     */
    public static <T> ResponseWrapper<T> error(APIErrorResponse error) {
        return new ResponseWrapper<>(false, null, error);
    }
}
