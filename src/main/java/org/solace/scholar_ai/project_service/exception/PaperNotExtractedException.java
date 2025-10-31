package org.solace.scholar_ai.project_service.exception;

import java.util.UUID;
import lombok.Getter;

/**
 * Exception thrown when attempting to perform operations on a paper that
 * hasn't been extracted yet. This exception is typically raised when trying
 * to generate summaries, perform gap analysis, or access paper content
 * before the extraction process has completed.
 */
@Getter
public class PaperNotExtractedException extends RuntimeException {
    private final UUID paperId;

    /**
     * Creates a new PaperNotExtractedException with a custom message.
     *
     * @param message The error message
     */
    public PaperNotExtractedException(String message) {
        super(message);
        this.paperId = null;
    }

    /**
     * Creates a new PaperNotExtractedException with a default message for the given paper ID.
     *
     * @param paperId The UUID of the paper that has not been extracted
     */
    public PaperNotExtractedException(UUID paperId) {
        super("Paper has not been extracted yet. Please wait for extraction to complete. Paper ID: " + paperId);
        this.paperId = paperId;
    }

    /**
     * Creates a new PaperNotExtractedException with a custom message and paper ID.
     *
     * @param message The error message
     * @param paperId The UUID of the paper that has not been extracted
     */
    public PaperNotExtractedException(String message, UUID paperId) {
        super(message);
        this.paperId = paperId;
    }

    /**
     * Creates a new PaperNotExtractedException with a custom message and cause.
     *
     * @param message The error message
     * @param cause   The cause of this exception
     */
    public PaperNotExtractedException(String message, Throwable cause) {
        super(message, cause);
        this.paperId = null;
    }
}
