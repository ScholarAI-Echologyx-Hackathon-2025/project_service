package org.solace.scholar_ai.project_service.constant.todo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

/**
 * Enumeration representing the status of a todo item.
 * Each todo item can be in one of these states to track its lifecycle.
 */
public enum TodoStatus {
    /** Todo item is pending and not yet started. */
    PENDING,
    /** Todo item is currently in progress. */
    IN_PROGRESS,
    /** Todo item has been completed. */
    COMPLETED,
    /** Todo item has been cancelled. */
    CANCELLED;

    /**
     * Converts the enum value to its JSON representation (lowercase string).
     *
     * @return The lowercase string representation of this enum value
     */
    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }

    /**
     * Creates a TodoStatus enum value from a string representation.
     * Performs case-insensitive matching and handles underscores flexibly.
     *
     * @param value The string value to convert (can be null)
     * @return The corresponding TodoStatus enum value
     * @throws IllegalArgumentException if the value does not match any enum constant
     */
    @JsonCreator
    public static TodoStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(TodoStatus.values())
                .filter(status -> status.name().replace("_", "").equalsIgnoreCase(value.replace("_", "")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown enum type " + value + ", Allowed values are "
                        + Stream.of(values()).map(TodoStatus::name).toString()));
    }
}
