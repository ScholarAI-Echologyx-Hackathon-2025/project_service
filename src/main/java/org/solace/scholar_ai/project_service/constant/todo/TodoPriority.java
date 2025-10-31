package org.solace.scholar_ai.project_service.constant.todo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

/**
 * Enumeration representing the priority level of a todo item.
 * Priorities help users and the system organize and prioritize tasks.
 */
public enum TodoPriority {
    /** Low priority - can be deferred if needed. */
    LOW,
    /** Medium priority - standard priority level. */
    MEDIUM,
    /** High priority - should be addressed soon. */
    HIGH,
    /** Urgent priority - requires immediate attention. */
    URGENT;

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
     * Creates a TodoPriority enum value from a string representation.
     * Performs case-insensitive matching.
     *
     * @param value The string value to convert (can be null)
     * @return The corresponding TodoPriority enum value
     * @throws IllegalArgumentException if the value does not match any enum constant
     */
    @JsonCreator
    public static TodoPriority fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(TodoPriority.values())
                .filter(priority -> priority.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown enum type " + value + ", Allowed values are "
                        + Stream.of(values()).map(TodoPriority::name).toString()));
    }
}
