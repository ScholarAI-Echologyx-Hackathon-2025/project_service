package org.solace.scholar_ai.project_service.mapping.note;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.solace.scholar_ai.project_service.dto.author.AuthorDto;
import org.solace.scholar_ai.project_service.dto.note.PaperSuggestionDto;
import org.solace.scholar_ai.project_service.dto.paper.PaperMetadataDto;

/**
 * MapStruct mapper interface for converting paper metadata to suggestion DTOs.
 * Used for paper mention functionality in notes, generating display text
 * and formatting author information.
 */
@Mapper(componentModel = "spring")
public interface PaperMentionMapper {

    /** Singleton instance of the mapper. */
    PaperMentionMapper INSTANCE = Mappers.getMapper(PaperMentionMapper.class);

    /**
     * Converts a PaperMetadataDto to a PaperSuggestionDto for mention suggestions.
     * Generates a display text combining title and publication year.
     *
     * @param dto The PaperMetadataDto to convert
     * @return A PaperSuggestionDto suitable for mention suggestions
     */
    @Mapping(target = "displayText", expression = "java(generateDisplayText(dto))")
    @Mapping(target = "authors", source = "authors")
    PaperSuggestionDto toSuggestionDto(PaperMetadataDto dto);

    /**
     * Generates a display text for a paper by combining title and year.
     * Truncates long titles to 50 characters for better UI display.
     *
     * @param dto The PaperMetadataDto containing paper information
     * @return A formatted display string (e.g., "Paper Title (2024)")
     */
    default String generateDisplayText(PaperMetadataDto dto) {
        StringBuilder displayText = new StringBuilder();

        // Add title (truncated if too long)
        String title = dto.title();
        if (title != null) {
            if (title.length() > 50) {
                title = title.substring(0, 47) + "...";
            }
            displayText.append(title);
        }

        // Add year if available
        if (dto.publicationDate() != null) {
            displayText.append(" (").append(dto.publicationDate().getYear()).append(")");
        }

        return displayText.toString();
    }

    /**
     * Custom mapping method to convert a list of AuthorDtos to a list of author name strings.
     * Extracts the name field from each AuthorDto.
     *
     * @param authors The list of AuthorDtos to convert
     * @return A list of author names, or null if the input list is null
     */
    default List<String> mapAuthors(List<AuthorDto> authors) {
        if (authors == null) {
            return null;
        }
        return authors.stream().map(AuthorDto::name).toList();
    }
}
