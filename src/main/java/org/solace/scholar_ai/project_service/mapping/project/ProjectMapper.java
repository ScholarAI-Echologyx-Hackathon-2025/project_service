package org.solace.scholar_ai.project_service.mapping.project;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.solace.scholar_ai.project_service.dto.project.CreateProjectDto;
import org.solace.scholar_ai.project_service.dto.project.ProjectDto;
import org.solace.scholar_ai.project_service.dto.project.UpdateProjectDto;
import org.solace.scholar_ai.project_service.model.project.Project;

/**
 * MapStruct mapper interface for converting between Project entity and DTOs.
 * Handles status enum conversions and string/list conversions for topics and tags.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper {

    /** Singleton instance of the mapper. */
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    /**
     * Converts a Project entity to a ProjectDto.
     * Converts status enum to string and comma-separated strings to lists.
     *
     * @param entity The Project entity to convert
     * @return The corresponding ProjectDto
     */
    @Mapping(target = "status", source = "status", qualifiedByName = "statusEnumToString")
    @Mapping(target = "topics", source = "topics", qualifiedByName = "stringToList")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "stringToList")
    ProjectDto toDto(Project entity);

    /**
     * Converts a ProjectDto to a Project entity.
     * Converts status string to enum and lists to comma-separated strings.
     *
     * @param dto The ProjectDto to convert
     * @return The corresponding Project entity
     */
    @Mapping(target = "id", ignore = true) // ID will be generated
    @Mapping(target = "createdAt", ignore = true) // Auto-generated
    @Mapping(target = "updatedAt", ignore = true) // Auto-generated
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatusEnum")
    @Mapping(target = "topics", source = "topics", qualifiedByName = "listToString")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "listToString")
    Project toEntity(ProjectDto dto);

    /**
     * Converts a ProjectDto to a Project entity for updates, preserving existing entity data.
     * Used when updating an existing project with partial DTO data.
     *
     * @param dto            The ProjectDto containing updated data
     * @param existingEntity The existing Project entity to preserve unchanged fields
     * @return An updated Project entity
     */
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "domain", source = "dto.domain")
    @Mapping(target = "userId", source = "dto.userId")
    @Mapping(target = "progress", source = "dto.progress")
    @Mapping(target = "totalPapers", source = "dto.totalPapers")
    @Mapping(target = "activeTasks", source = "dto.activeTasks")
    @Mapping(target = "lastActivity", source = "dto.lastActivity")
    @Mapping(target = "isStarred", source = "dto.isStarred")
    @Mapping(target = "createdAt", ignore = true) // Don't update creation time
    @Mapping(target = "updatedAt", ignore = true) // Auto-generated
    @Mapping(target = "status", source = "dto.status", qualifiedByName = "stringToStatusEnum")
    @Mapping(target = "topics", source = "dto.topics", qualifiedByName = "listToString")
    @Mapping(target = "tags", source = "dto.tags", qualifiedByName = "listToString")
    Project toEntityForUpdate(ProjectDto dto, Project existingEntity);

    /**
     * Converts a Project.Status enum to its string representation.
     *
     * @param status The Project.Status enum value
     * @return The string representation of the status, or null if status is null
     */
    @Named("statusEnumToString")
    default String statusEnumToString(Project.Status status) {
        return status != null ? status.name() : null;
    }

    /**
     * Converts a string to a Project.Status enum.
     * Returns ACTIVE as default if the string is null, empty, or invalid.
     *
     * @param status The status string to convert
     * @return The corresponding Project.Status enum value
     */
    @Named("stringToStatusEnum")
    default Project.Status stringToStatusEnum(String status) {
        if (status == null || status.trim().isEmpty()) {
            return Project.Status.ACTIVE; // Default status
        }
        try {
            return Project.Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Project.Status.ACTIVE; // Fallback to default
        }
    }

    /**
     * Converts a comma-separated string to a list of strings.
     *
     * @param str The comma-separated string to convert
     * @return A list of strings, or null if the string is null or empty
     */
    @Named("stringToList")
    default List<String> stringToList(String str) {
        if (str == null || str.trim().isEmpty()) return null;
        return Arrays.asList(str.split(","));
    }

    /**
     * Converts a list of strings to a comma-separated string.
     *
     * @param list The list of strings to convert
     * @return A comma-separated string, or null if the list is null or empty
     */
    @Named("listToString")
    default String listToString(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        return String.join(",", list);
    }

    /**
     * Converts a CreateProjectDto to a Project entity for new project creation.
     * Sets default values for status, progress, and boolean fields.
     *
     * @param dto     The CreateProjectDto containing project data
     * @param userId  The UUID of the user creating the project
     * @return A new Project entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "status", expression = "java(Project.Status.ACTIVE)")
    @Mapping(target = "progress", constant = "0")
    @Mapping(target = "totalPapers", constant = "0")
    @Mapping(target = "activeTasks", constant = "0")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastActivity", ignore = true)
    @Mapping(target = "isStarred", constant = "false")
    @Mapping(target = "topics", source = "dto.topics", qualifiedByName = "listToString")
    @Mapping(target = "tags", source = "dto.tags", qualifiedByName = "listToString")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "domain", source = "dto.domain")
    Project fromCreateDto(CreateProjectDto dto, UUID userId);

    /**
     * Converts an UpdateProjectDto to a Project entity for updates.
     * Ignores system-managed fields like ID, userId, timestamps, and metrics.
     *
     * @param dto The UpdateProjectDto containing updated project data
     * @return A Project entity with updated fields
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "totalPapers", ignore = true)
    @Mapping(target = "activeTasks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatusEnum")
    @Mapping(target = "topics", source = "topics", qualifiedByName = "listToString")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "listToString")
    Project fromUpdateDto(UpdateProjectDto dto);
}
