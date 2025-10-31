package org.solace.scholar_ai.project_service.mapping.note;

import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.solace.scholar_ai.project_service.dto.note.CreateNoteDto;
import org.solace.scholar_ai.project_service.dto.note.NoteDto;
import org.solace.scholar_ai.project_service.dto.note.UpdateNoteDto;
import org.solace.scholar_ai.project_service.model.note.ProjectNote;

/**
 * MapStruct mapper interface for converting between ProjectNote entity and DTOs.
 * Handles mapping for note creation, updates, and retrieval operations.
 */
@Mapper(componentModel = "spring")
public interface ProjectNoteMapper {

    /** Singleton instance of the mapper. */
    ProjectNoteMapper INSTANCE = Mappers.getMapper(ProjectNoteMapper.class);

    /**
     * Converts a ProjectNote entity to a NoteDto.
     *
     * @param entity The ProjectNote entity to convert
     * @return The corresponding NoteDto
     */
    NoteDto toDto(ProjectNote entity);

    /**
     * Converts a CreateNoteDto to a ProjectNote entity for new note creation.
     * Ignores auto-generated fields and sets default values.
     *
     * @param dto       The CreateNoteDto containing note data
     * @param projectId The UUID of the project this note belongs to
     * @return A new ProjectNote entity ready for persistence
     */
    @Mapping(target = "id", ignore = true) // ID will be generated
    @Mapping(target = "createdAt", ignore = true) // Auto-generated
    @Mapping(target = "updatedAt", ignore = true) // Auto-generated
    @Mapping(target = "isFavorite", constant = "false") // Default to false
    ProjectNote fromCreateDto(CreateNoteDto dto, UUID projectId);

    /**
     * Converts an UpdateNoteDto to a ProjectNote entity for updates.
     * Preserves system-managed fields like ID, project ID, and timestamps.
     *
     * @param dto The UpdateNoteDto containing updated note data
     * @return A ProjectNote entity with updated fields
     */
    @Mapping(target = "id", ignore = true) // Don't update ID
    @Mapping(target = "projectId", ignore = true) // Don't update project ID
    @Mapping(target = "createdAt", ignore = true) // Don't update creation time
    @Mapping(target = "updatedAt", ignore = true) // Auto-generated
    @Mapping(target = "isFavorite", ignore = true) // Don't update favorite status
    ProjectNote fromUpdateDto(UpdateNoteDto dto);
}
