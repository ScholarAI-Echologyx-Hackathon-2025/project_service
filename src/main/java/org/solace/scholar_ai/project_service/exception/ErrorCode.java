package org.solace.scholar_ai.project_service.exception;

import lombok.Getter;

/**
 * Enumeration of error codes used across the application.
 * Each error code includes a user-friendly suggestion for resolution.
 * These codes help provide consistent error handling and better user experience
 * by offering actionable guidance when errors occur.
 */
@Getter
public enum ErrorCode {
    /** Client Validation Errors - Invalid request format or validation failures. */
    INVALID_REQUEST("Please review the validation errors and correct your request."),
    /** Invalid parameter type provided in the request. */
    INVALID_PARAMETER_TYPE("Please ensure the parameter value matches the required type."),
    /** Constraint violation in the request data. */
    CONSTRAINT_VIOLATION("Please check the input constraints in the API documentation."),
    /** Required parameter is missing from the request. */
    MISSING_PARAMETER("Please include all required parameters as specified in the documentation."),
    /** Malformed JSON in the request body. */
    MALFORMED_JSON("Please verify the JSON syntax and data types in your request."),
    /** Invalid argument value provided. */
    INVALID_ARGUMENT("Please check the argument values against the API specifications."),
    /** Unsupported media type in the request. */
    UNSUPPORTED_MEDIA_TYPE("Please use one of the supported media types for this endpoint."),
    /** Authentication & Authorization Errors - Access denied due to insufficient permissions. */
    ACCESS_DENIED("Please ensure you have the necessary permissions or authenticate properly."),
    /** Resource & Method Errors - Requested resource not found. */
    RESOURCE_NOT_FOUND("Please verify the requested resource exists and the URL is correct."),
    /** HTTP method not allowed for the requested endpoint. */
    METHOD_NOT_ALLOWED("Please use one of the supported HTTP methods for this endpoint."),
    /** System Errors - Internal server error. */
    INTERNAL_ERROR("Please try again later or contact support if the issue persists."),
    /** Failed to send email notification. */
    EMAIL_SENDING_FAILED("Please check the email service configuration and try again."),
    /** External API request failed. */
    EXTERNAL_API_ERROR("External API request failed. Please try again later or contact support."),
    /** External service communication failed. */
    EXTERNAL_SERVICE_ERROR("External service communication failed. Please try again later."),
    /** Duplicate resource creation attempted. */
    DUPLICATE("Please ensure the resource you're trying to create does not already exist."),
    /** Validation error in the request data. */
    VALIDATION_ERROR("Please review the validation errors and correct your request."),
    /** Configuration error in the application. */
    CONFIGURATION_ERROR("Please check the application configuration for any issues."),
    /** Author-specific errors - Author not found. */
    AUTHOR_NOT_FOUND("The requested author could not be found. Please check the author name or ID."),
    /** Error processing or converting data. */
    DATA_CONVERSION_ERROR("Error processing data. Please verify the data format and try again."),
    /** Paper-specific errors - Paper not found. */
    PAPER_NOT_FOUND("The requested paper could not be found. Please check the paper ID."),
    /** Paper has not been extracted yet. */
    PAPER_NOT_EXTRACTED("The paper has not been extracted yet. Please extract the paper first."),
    /** Gap analysis specific errors - Gap analysis not found. */
    GAP_ANALYSIS_NOT_FOUND("The requested gap analysis could not be found. Please check the gap analysis ID."),
    /** Failed to initiate gap analysis request. */
    GAP_ANALYSIS_REQUEST_FAILED("Failed to initiate gap analysis. Please try again later."),
    /** Invalid operation for the current resource state. */
    INVALID_OPERATION("The requested operation is not valid for the current state of the resource.");

    private final String suggestion;

    /**
     * Creates a new ErrorCode with the specified user-friendly suggestion.
     *
     * @param suggestion A user-friendly suggestion message for resolving this error
     */
    ErrorCode(String suggestion) {
        this.suggestion = suggestion;
    }
}
