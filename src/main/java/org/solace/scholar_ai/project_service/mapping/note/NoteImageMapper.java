package org.solace.scholar_ai.project_service.mapping.note;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.solace.scholar_ai.project_service.dto.note.ImageUploadDto;
import org.solace.scholar_ai.project_service.model.note.NoteImage;

/**
 * MapStruct mapper interface for converting between NoteImage entity and ImageUploadDto.
 * Handles mapping for note image uploads and retrieval.
 */
@Mapper(componentModel = "spring")
public interface NoteImageMapper {

    /** Singleton instance of the mapper. */
    NoteImageMapper INSTANCE = Mappers.getMapper(NoteImageMapper.class);

    /**
     * Converts a NoteImage entity to an ImageUploadDto.
     * Maps the entity ID to imageId and includes the image URL.
     *
     * @param entity   The NoteImage entity to convert
     * @param imageUrl The URL of the image
     * @return The corresponding ImageUploadDto
     */
    @Mapping(target = "imageId", source = "entity.id")
    @Mapping(target = "imageUrl", source = "imageUrl")
    ImageUploadDto toDto(NoteImage entity, String imageUrl);
}
