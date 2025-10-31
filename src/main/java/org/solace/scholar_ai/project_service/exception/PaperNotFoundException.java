package org.solace.scholar_ai.project_service.exception;

import java.util.UUID;
import lombok.Getter;

/**
 * Exception thrown when a requested paper cannot be found in the system.
 * This exception is raised when attempting to access, update, or delete
 * a paper that does not exist or is not accessible.
 */
@Getter
public class PaperNotFoundException extends RuntimeException {
    private final UUID paperId;

    /**
     * Creates a new PaperNotFoundException with a custom message.
     *
     * @param message The error message
     */
    public PaperNotFoundException(String message) {
        super(message);
        this.paperId = null;
    }

    /**
     * Creates a new PaperNotFoundException with a default message for the given
     * paper ID.
     *
     * @param paperId The UUID of the paper that was not found
     */
    public PaperNotFoundException(UUID paperId) {
        super("Paper not found with ID: " + paperId);
        this.paperId = paperId;
    }

    /**
     * Creates a new PaperNotFoundException with a custom message and paper ID.
     *
     * @param message The error message
     * @param paperId The UUID of the paper that was not found
     */
    public PaperNotFoundException(String message, UUID paperId) {
        super(message);
        this.paperId = paperId;
    }

    /**
     * Creates a new PaperNotFoundException with a custom message and cause.
     *
     * @param message The error message
     * @param cause   The cause of this exception
     */
    public PaperNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.paperId = null;
    }
}
