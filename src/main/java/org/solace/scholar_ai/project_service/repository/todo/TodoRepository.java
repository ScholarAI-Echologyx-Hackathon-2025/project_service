package org.solace.scholar_ai.project_service.repository.todo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.solace.scholar_ai.project_service.constant.todo.TodoCategory;
import org.solace.scholar_ai.project_service.constant.todo.TodoPriority;
import org.solace.scholar_ai.project_service.constant.todo.TodoStatus;
import org.solace.scholar_ai.project_service.model.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for Todo entities.
 * Provides methods for querying todos by various criteria including status, priority,
 * category, due dates, tags, and project associations.
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, String>, JpaSpecificationExecutor<Todo> {

    /**
     * Finds all todos with any of the specified statuses.
     *
     * @param statuses The list of statuses to filter by
     * @return A list of todos matching the statuses
     */
    List<Todo> findByStatusIn(List<TodoStatus> statuses);

    /**
     * Finds all todos with any of the specified priorities.
     *
     * @param priorities The list of priorities to filter by
     * @return A list of todos matching the priorities
     */
    List<Todo> findByPriorityIn(List<TodoPriority> priorities);

    /**
     * Finds all todos with any of the specified categories.
     *
     * @param categories The list of categories to filter by
     * @return A list of todos matching the categories
     */
    List<Todo> findByCategoryIn(List<TodoCategory> categories);

    /**
     * Finds all todos with due dates within the specified range.
     *
     * @param start The start of the date range (inclusive)
     * @param end   The end of the date range (inclusive)
     * @return A list of todos with due dates in the range
     */
    List<Todo> findByDueDateBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Finds all overdue todos (due date is in the past and status is not completed).
     *
     * @param now The current date/time for comparison
     * @return A list of overdue todos
     */
    @Query("SELECT t FROM Todo t WHERE t.dueDate < :now AND t.status != 'completed'")
    List<Todo> findOverdueTodos(@Param("now") LocalDateTime now);

    /**
     * Finds all todos due today.
     *
     * @param today The current date/time
     * @return A list of todos due today
     */
    @Query("SELECT t FROM Todo t WHERE DATE(t.dueDate) = DATE(:today)")
    List<Todo> findTodosDueToday(@Param("today") LocalDateTime today);

    /**
     * Finds all todos due this week.
     *
     * @param startOfWeek The start of the week
     * @param endOfWeek    The end of the week
     * @return A list of todos due this week
     */
    @Query("SELECT t FROM Todo t WHERE t.dueDate BETWEEN :startOfWeek AND :endOfWeek")
    List<Todo> findTodosDueThisWeek(
            @Param("startOfWeek") LocalDateTime startOfWeek, @Param("endOfWeek") LocalDateTime endOfWeek);

    /**
     * Searches todos by title or description using case-insensitive partial matching.
     *
     * @param search The search keyword
     * @return A list of todos matching the search criteria
     */
    @Query("SELECT t FROM Todo t WHERE " + "LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Todo> searchTodos(@Param("search") String search);

    /**
     * Finds all todos that have any of the specified tags.
     *
     * @param tags The set of tags to filter by
     * @return A list of distinct todos matching any of the tags
     */
    @Query("SELECT DISTINCT t FROM Todo t JOIN t.tags tag WHERE tag IN :tags")
    List<Todo> findByTagsIn(@Param("tags") Set<String> tags);

    /**
     * Finds all todos associated with a specific project.
     *
     * @param projectId The ID of the project
     * @return A list of todos related to the project
     */
    List<Todo> findByRelatedProjectId(String projectId);

    /**
     * Counts todos with a specific status.
     *
     * @param status The status to count
     * @return The count of todos with the specified status
     */
    long countByStatus(TodoStatus status);

    /**
     * Counts todos with a specific priority.
     *
     * @param priority The priority to count
     * @return The count of todos with the specified priority
     */
    long countByPriority(TodoPriority priority);

    /**
     * Finds only the IDs of todos associated with a specific project.
     *
     * @param projectId The ID of the project
     * @return A list of todo IDs related to the project
     */
    @Query("SELECT t.id FROM Todo t WHERE t.relatedProjectId = :projectId")
    List<String> findIdsByProjectId(@Param("projectId") String projectId);

    /**
     * Deletes all todos associated with a specific project.
     *
     * @param projectId The ID of the project
     */
    void deleteByRelatedProjectId(String projectId);
}
