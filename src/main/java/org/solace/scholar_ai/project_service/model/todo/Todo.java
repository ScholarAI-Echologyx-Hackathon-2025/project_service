package org.solace.scholar_ai.project_service.model.todo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import lombok.*;
import org.solace.scholar_ai.project_service.constant.todo.TodoCategory;
import org.solace.scholar_ai.project_service.constant.todo.TodoPriority;
import org.solace.scholar_ai.project_service.constant.todo.TodoStatus;

/**
 * Entity representing a todo item in the research project management system.
 * Supports subtasks, reminders, tags, and relationships to projects and papers.
 * Tracks status, priority, category, time estimates, and completion information.
 */
@Entity
@Table(name = "todos", indexes = @Index(columnList = "userId"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(nullable = false, updatable = false)
    private String userId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoCategory category;

    private LocalDateTime dueDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    private Integer estimatedTime; // in minutes

    private Integer actualTime; // in minutes

    private String relatedProjectId;

    private String relatedPaperId;

    @ElementCollection
    @CollectionTable(name = "todo_tags", joinColumns = @JoinColumn(name = "todo_id"))
    @Column(name = "tag")
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<TodoSubtask> subtasks = new ArrayList<>();

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<TodoReminder> reminders = new ArrayList<>();

    /**
     * Adds a subtask to this todo item.
     *
     * @param subtask The subtask to add
     */
    public void addSubtask(TodoSubtask subtask) {
        subtasks.add(subtask);
        subtask.setTodo(this);
    }

    /**
     * Removes a subtask from this todo item.
     *
     * @param subtask The subtask to remove
     */
    public void removeSubtask(TodoSubtask subtask) {
        subtasks.remove(subtask);
        subtask.setTodo(null);
    }

    /**
     * Adds a reminder to this todo item.
     *
     * @param reminder The reminder to add
     */
    public void addReminder(TodoReminder reminder) {
        reminders.add(reminder);
        reminder.setTodo(this);
    }

    /**
     * Removes a reminder from this todo item.
     *
     * @param reminder The reminder to remove
     */
    public void removeReminder(TodoReminder reminder) {
        reminders.remove(reminder);
        reminder.setTodo(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return getId() != null && Objects.equals(getId(), todo.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
