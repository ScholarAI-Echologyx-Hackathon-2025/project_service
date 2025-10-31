package org.solace.scholar_ai.project_service.mapping.todo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.solace.scholar_ai.project_service.dto.todo.request.TodoCreateReqDTO;
import org.solace.scholar_ai.project_service.dto.todo.response.TodoResponseDTO;
import org.solace.scholar_ai.project_service.model.todo.Todo;
import org.solace.scholar_ai.project_service.model.todo.TodoSubtask;

/**
 * MapStruct mapper interface for converting between Todo entity and DTOs.
 * Handles date/time string conversions and maps todo creation requests,
 * responses, and subtasks.
 */
@Mapper(
        componentModel = "spring",
        imports = {LocalDateTime.class})
public interface TodoMapper {

    /**
     * Converts a TodoCreateReqDTO to a Todo entity for new todo creation.
     * Sets default status to PENDING and handles date/time conversions.
     *
     * @param request The TodoCreateReqDTO containing todo data
     * @return A new Todo entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "dueDate", source = "dueDate", qualifiedByName = "stringToLocalDateTime")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "actualTime", ignore = true)
    @Mapping(target = "relatedPaperId", ignore = true)
    @Mapping(target = "subtasks", ignore = true)
    @Mapping(target = "reminders", ignore = true)
    Todo todoCreateRequestToTodo(TodoCreateReqDTO request);

    /**
     * Converts a Todo entity to a TodoResponseDTO.
     * Converts all LocalDateTime fields to ISO 8601 formatted strings.
     *
     * @param todo The Todo entity to convert
     * @return The corresponding TodoResponseDTO
     */
    @Mapping(source = "dueDate", target = "dueDate", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "completedAt", target = "completedAt", qualifiedByName = "localDateTimeToString")
    TodoResponseDTO todoToTodoResponse(Todo todo);

    /**
     * Converts a list of Todo entities to a list of TodoResponseDTOs.
     *
     * @param todos The list of Todo entities to convert
     * @return A list of corresponding TodoResponseDTOs
     */
    List<TodoResponseDTO> todosToTodoResponses(List<Todo> todos);

    /**
     * Converts a TodoSubtask entity to a TodoResponseDTO.SubtaskResponse.
     *
     * @param subtask The TodoSubtask entity to convert
     * @return The corresponding SubtaskResponse DTO
     */
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localDateTimeToString")
    TodoResponseDTO.SubtaskResponse subtaskToSubtaskResponse(TodoSubtask subtask);

    /**
     * Converts a LocalDateTime to an ISO 8601 formatted string.
     * Returns null if the dateTime is null.
     *
     * @param dateTime The LocalDateTime to convert
     * @return An ISO 8601 formatted string, or null if dateTime is null
     */
    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * Converts a date/time string to a LocalDateTime.
     * Supports ISO 8601 formats including those with timezones (Z, +, -).
     * Tries multiple parsing strategies for compatibility.
     *
     * @param dateString The date/time string to convert
     * @return A LocalDateTime parsed from the string, or null if the string is null or empty
     * @throws IllegalArgumentException if the string cannot be parsed
     */
    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) return null;
        try {
            // Handle ISO strings with milliseconds and timezone (e.g., "2025-07-07T08:30:00.000Z")
            if (dateString.contains("Z") || dateString.contains("+") || dateString.contains("-")) {
                return java.time.OffsetDateTime.parse(dateString).toLocalDateTime();
            }
            // Handle local date time strings
            return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            // Fallback: try different common formats
            try {
                return LocalDateTime.parse(dateString.replace("Z", ""), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception e2) {
                throw new IllegalArgumentException("Unable to parse date string: " + dateString, e2);
            }
        }
    }
}
