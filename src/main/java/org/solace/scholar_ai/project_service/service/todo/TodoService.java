package org.solace.scholar_ai.project_service.service.todo;

import java.util.List;
import org.solace.scholar_ai.project_service.dto.todo.request.TodoCreateReqDTO;
import org.solace.scholar_ai.project_service.dto.todo.request.TodoFiltersReqDTO;
import org.solace.scholar_ai.project_service.dto.todo.request.TodoStatusUpdateReqDTO;
import org.solace.scholar_ai.project_service.dto.todo.request.TodoUpdateReqDTO;
import org.solace.scholar_ai.project_service.dto.todo.response.TodoResponseDTO;
import org.solace.scholar_ai.project_service.dto.todo.response.TodoSummaryResDTO;

/**
 * Service interface for managing todo items.
 * Provides operations for creating, updating, deleting, and querying todos,
 * as well as managing subtasks and retrieving summary statistics.
 */
public interface TodoService {
    /**
     * Creates a new todo item.
     *
     * @param request The todo creation request
     * @return The created todo response DTO
     * @throws Exception if creation fails
     */
    TodoResponseDTO createTodo(TodoCreateReqDTO request) throws Exception;

    /**
     * Updates the status of a todo item.
     *
     * @param id           The ID of the todo item
     * @param statusUpdate The status update request
     * @return The updated todo response DTO
     * @throws Exception if update fails or todo not found
     */
    TodoResponseDTO updateStatus(String id, TodoStatusUpdateReqDTO statusUpdate) throws Exception;

    /**
     * Updates a todo item with new information.
     *
     * @param id          The ID of the todo item
     * @param updateReqDTO The todo update request
     * @return The updated todo response DTO
     * @throws Exception if update fails or todo not found
     */
    TodoResponseDTO updateTodo(String id, TodoUpdateReqDTO updateReqDTO) throws Exception;

    /**
     * Deletes a todo item.
     *
     * @param id The ID of the todo item to delete
     * @throws Exception if deletion fails or todo not found
     */
    void deleteTodo(String id) throws Exception;

    /**
     * Retrieves a todo item by its ID.
     *
     * @param id The ID of the todo item
     * @return The todo response DTO
     * @throws Exception if todo not found
     */
    TodoResponseDTO getTodoById(String id) throws Exception;

    /**
     * Filters and retrieves todos based on specified criteria.
     *
     * @param filters The filter criteria for todos
     * @return A list of filtered todo response DTOs
     * @throws Exception if filtering fails
     */
    List<TodoResponseDTO> filterTodos(TodoFiltersReqDTO filters) throws Exception;

    /**
     * Retrieves summary statistics for todos belonging to a user.
     *
     * @param userId The ID of the user
     * @return The todo summary response DTO
     * @throws Exception if summary retrieval fails
     */
    TodoSummaryResDTO getSummary(String userId) throws Exception;

    /**
     * Adds a subtask to a todo item.
     *
     * @param todoId       The ID of the parent todo item
     * @param subtaskTitle The title of the subtask to add
     * @return The updated todo response DTO with the new subtask
     * @throws Exception if addition fails or todo not found
     */
    TodoResponseDTO addSubtask(String todoId, String subtaskTitle) throws Exception;

    /**
     * Toggles the completion status of a subtask.
     *
     * @param subtaskId The ID of the subtask to toggle
     * @return The updated todo response DTO
     * @throws Exception if toggle fails or subtask not found
     */
    TodoResponseDTO toggleSubtaskCompletion(String subtaskId) throws Exception;
}
