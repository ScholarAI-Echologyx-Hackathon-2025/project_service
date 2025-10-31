package org.solace.scholar_ai.project_service.exception;

/**
 * Exception thrown when a requested chat session cannot be found in the system.
 * This exception is raised when attempting to access, update, or delete
 * a chat session that does not exist or is not accessible.
 */
public class ChatSessionNotFoundException extends RuntimeException {

    /**
     * Creates a new ChatSessionNotFoundException with a custom message.
     *
     * @param message The error message
     */
    public ChatSessionNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new ChatSessionNotFoundException with a custom message and cause.
     *
     * @param message The error message
     * @param cause   The cause of this exception
     */
    public ChatSessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
