package org.solace.scholar_ai.project_service.constant;

/**
 * Enumeration of command types that can be processed by the system.
 * These commands represent various actions that can be performed,
 * primarily related to todo management and paper searching.
 */
public enum CommandType {
    /** Command to create a new todo item. */
    CREATE_TODO,
    /** Command to update an existing todo item. */
    UPDATE_TODO,
    /** Command to delete a todo item. */
    DELETE_TODO,
    /** Command to search for todo items. */
    SEARCH_TODO,
    /** Command to summarize multiple todo items. */
    SUMMARIZE_TODOS,
    /** Command to search for papers. */
    SEARCH_PAPERS,
    /** Command for general questions that don't fit other categories. */
    GENERAL_QUESTION,
    /** Command type when the requested command cannot be identified. */
    UNKNOWN
}
