package org.solace.scholar_ai.project_service.constant.todo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

/**
 * Enumeration representing categories for todo items.
 * Categories help organize todos by their purpose and context,
 * spanning research lifecycle, project collaboration, and personal productivity.
 */
public enum TodoCategory {
    /** Research Lifecycle - Literature review tasks. */
    LITERATURE_REVIEW,
    /** Research Lifecycle - Experiment-related tasks. */
    EXPERIMENT,
    /** Research Lifecycle - Data collection tasks. */
    DATA_COLLECTION,
    /** Research Lifecycle - Data analysis tasks. */
    DATA_ANALYSIS,
    /** Research Lifecycle - Modeling tasks. */
    MODELING,
    /** Research Lifecycle - Writing tasks. */
    WRITING,
    /** Research Lifecycle - Review tasks. */
    REVIEW,
    /** Research Lifecycle - Paper submission tasks. */
    SUBMISSION,
    /** Research Lifecycle - Presentation tasks. */
    PRESENTATION,

    /** Project & Collaboration - Collaboration-related tasks. */
    COLLABORATION,
    /** Project & Collaboration - Meeting-related tasks. */
    MEETING,
    /** Project & Collaboration - Deadline-related tasks. */
    DEADLINE,
    /** Project & Collaboration - Funding-related tasks. */
    FUNDING,
    /** Project & Collaboration - Administrative tasks. */
    ADMINISTRATIVE,

    /** Personal Productivity - Personal tasks. */
    PERSONAL,
    /** Personal Productivity - Miscellaneous category for todos that don't fit other categories. */
    MISC;

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
     * Creates a TodoCategory enum value from a string representation.
     * Performs case-insensitive matching.
     *
     * @param value The string value to convert (can be null)
     * @return The corresponding TodoCategory enum value
     * @throws IllegalArgumentException if the value does not match any enum constant
     */
    @JsonCreator
    public static TodoCategory fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(TodoCategory.values())
                .filter(category -> category.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown enum type " + value + ", Allowed values are "
                        + Stream.of(values()).map(TodoCategory::name).toList()));
    }
}
