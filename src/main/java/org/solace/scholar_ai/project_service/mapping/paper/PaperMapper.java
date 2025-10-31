package org.solace.scholar_ai.project_service.mapping.paper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.solace.scholar_ai.project_service.dto.paper.CreatePaperDto;
import org.solace.scholar_ai.project_service.dto.paper.PaperDto;
import org.solace.scholar_ai.project_service.dto.paper.PaperMetadataDto;
import org.solace.scholar_ai.project_service.dto.paper.UpdatePaperDto;
import org.solace.scholar_ai.project_service.mapping.author.AuthorMapper;
import org.solace.scholar_ai.project_service.model.paper.Paper;

/**
 * MapStruct mapper interface for converting between Paper entity and DTOs.
 * Handles complex mappings including authors, external IDs, venues, metrics,
 * and string/list conversions for publication types and fields of study.
 */
@Mapper(
        componentModel = "spring",
        uses = {AuthorMapper.class})
public interface PaperMapper {

    /** Singleton instance of the mapper. */
    PaperMapper INSTANCE = Mappers.getMapper(PaperMapper.class);

    /**
     * Converts a PaperMetadataDto to a Paper entity.
     * Maps authors, external IDs, venue, and metrics from the DTO.
     *
     * @param dto The PaperMetadataDto to convert
     * @return A new Paper entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "correlationId", ignore = true)
    @Mapping(target = "paperAuthors", source = "authors", qualifiedByName = "authorsToPaperAuthors")
    @Mapping(target = "externalIds", source = "externalIds", qualifiedByName = "externalIdsMapToEntities")
    @Mapping(target = "venue", source = ".", qualifiedByName = "mapVenue")
    @Mapping(target = "metrics", source = ".", qualifiedByName = "mapMetrics")
    @Mapping(target = "publicationTypes", source = "publicationTypes", qualifiedByName = "listToString")
    @Mapping(target = "fieldsOfStudy", source = "fieldsOfStudy", qualifiedByName = "listToString")
    Paper fromMetadataDto(PaperMetadataDto dto);

    /**
     * Converts a Paper entity to a PaperMetadataDto.
     * Extracts venue and metrics from nested entities and converts ID to string.
     *
     * @param entity The Paper entity to convert
     * @return The corresponding PaperMetadataDto
     */
    @Mapping(target = "id", expression = "java(entity.getId() != null ? entity.getId().toString() : null)")
    @Mapping(target = "authors", source = "paperAuthors", qualifiedByName = "paperAuthorsToAuthorDtos")
    @Mapping(target = "externalIds", source = "externalIds", qualifiedByName = "externalIdsToMap")
    @Mapping(target = "venueName", source = "venue.venueName")
    @Mapping(target = "publisher", source = "venue.publisher")
    @Mapping(target = "volume", source = "venue.volume")
    @Mapping(target = "issue", source = "venue.issue")
    @Mapping(target = "pages", source = "venue.pages")
    @Mapping(target = "citationCount", source = "metrics.citationCount")
    @Mapping(target = "referenceCount", source = "metrics.referenceCount")
    @Mapping(target = "influentialCitationCount", source = "metrics.influentialCitationCount")
    @Mapping(target = "publicationTypes", source = "publicationTypes", qualifiedByName = "stringToList")
    @Mapping(target = "fieldsOfStudy", source = "fieldsOfStudy", qualifiedByName = "stringToList")
    PaperMetadataDto toMetadataDto(Paper entity);

    /**
     * Converts a CreatePaperDto to a Paper entity for new paper creation.
     * Ignores venue and metrics which are typically set from external sources.
     *
     * @param dto The CreatePaperDto containing paper data
     * @return A new Paper entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paperAuthors", source = "authors", qualifiedByName = "authorsToPaperAuthors")
    @Mapping(target = "externalIds", source = "externalIds", qualifiedByName = "externalIdsMapToEntities")
    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "metrics", ignore = true)
    @Mapping(target = "publicationTypes", source = "publicationTypes", qualifiedByName = "listToString")
    @Mapping(target = "fieldsOfStudy", source = "fieldsOfStudy", qualifiedByName = "listToString")
    Paper fromCreateDto(CreatePaperDto dto);

    /**
     * Converts an UpdatePaperDto to a Paper entity for updates.
     * Ignores venue and metrics which are typically managed separately.
     *
     * @param dto The UpdatePaperDto containing updated paper data
     * @return A Paper entity with updated fields
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "correlationId", ignore = true)
    @Mapping(target = "paperAuthors", source = "authors", qualifiedByName = "authorsToPaperAuthors")
    @Mapping(target = "externalIds", source = "externalIds", qualifiedByName = "externalIdsMapToEntities")
    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "metrics", ignore = true)
    @Mapping(target = "publicationTypes", source = "publicationTypes", qualifiedByName = "listToString")
    @Mapping(target = "fieldsOfStudy", source = "fieldsOfStudy", qualifiedByName = "listToString")
    Paper fromUpdateDto(UpdatePaperDto dto);

    /**
     * Converts a Paper entity to a PaperDto.
     * Maps authors and external IDs from nested entities.
     *
     * @param entity The Paper entity to convert
     * @return The corresponding PaperDto
     */
    @Mapping(target = "authors", source = "paperAuthors", qualifiedByName = "paperAuthorsToAuthorDtos")
    @Mapping(target = "externalIds", source = "externalIds", qualifiedByName = "externalIdsToMap")
    @Mapping(target = "publicationTypes", source = "publicationTypes", qualifiedByName = "stringToList")
    @Mapping(target = "fieldsOfStudy", source = "fieldsOfStudy", qualifiedByName = "stringToList")
    PaperDto toDto(Paper entity);

    /**
     * Converts a list of Paper entities to a list of PaperDtos.
     *
     * @param entities The list of Paper entities to convert
     * @return A list of corresponding PaperDtos
     */
    List<PaperDto> toDtoList(List<Paper> entities);

    /**
     * Converts a list of PaperMetadataDtos to a list of Paper entities.
     *
     * @param dtos The list of PaperMetadataDtos to convert
     * @return A list of corresponding Paper entities
     */
    List<Paper> fromMetadataDtoList(List<PaperMetadataDto> dtos);

    /**
     * Helper method to convert a comma-separated string to a list of strings.
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
     * Helper method to convert a list of strings to a comma-separated string.
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
     * Helper method to convert a list of AuthorDtos to a list of PaperAuthor entities.
     * Creates PaperAuthor entities linking authors to papers.
     *
     * @param authors The list of AuthorDtos to convert
     * @return A list of PaperAuthor entities
     */
    @Named("authorsToPaperAuthors")
    default List<org.solace.scholar_ai.project_service.model.paper.PaperAuthor> authorsToPaperAuthors(
            List<org.solace.scholar_ai.project_service.dto.author.AuthorDto> authors) {
        if (authors == null) return null;
        return authors.stream()
                .map(authorDto -> {
                    org.solace.scholar_ai.project_service.model.author.Author author =
                            AuthorMapper.INSTANCE.toEntity(authorDto);
                    return new org.solace.scholar_ai.project_service.model.paper.PaperAuthor(null, author);
                })
                .collect(Collectors.toList());
    }

    /**
     * Helper method to convert a list of PaperAuthor entities to a list of AuthorDtos.
     * Extracts the Author entities from PaperAuthor wrappers.
     *
     * @param paperAuthors The list of PaperAuthor entities to convert
     * @return A list of AuthorDtos
     */
    @Named("paperAuthorsToAuthorDtos")
    default List<org.solace.scholar_ai.project_service.dto.author.AuthorDto> paperAuthorsToAuthorDtos(
            List<org.solace.scholar_ai.project_service.model.paper.PaperAuthor> paperAuthors) {
        if (paperAuthors == null) return null;
        return paperAuthors.stream()
                .map(pa -> AuthorMapper.INSTANCE.toDto(pa.getAuthor()))
                .collect(Collectors.toList());
    }

    /**
     * Helper method to convert a map of external IDs to a list of ExternalId entities.
     * Each map entry becomes an ExternalId entity with source and value.
     *
     * @param externalIdsMap The map of external IDs (source -> value)
     * @return A list of ExternalId entities, or null if the map is null
     */
    @Named("externalIdsMapToEntities")
    default List<org.solace.scholar_ai.project_service.model.paper.ExternalId> externalIdsMapToEntities(
            Map<String, Object> externalIdsMap) {
        if (externalIdsMap == null) return null;
        return externalIdsMap.entrySet().stream()
                .map(entry -> org.solace.scholar_ai.project_service.model.paper.ExternalId.builder()
                        .source(entry.getKey())
                        .value(entry.getValue().toString())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Helper method to convert a list of ExternalId entities to a map.
     * Creates a map where keys are sources and values are the external ID values.
     *
     * @param externalIds The list of ExternalId entities to convert
     * @return A map of external IDs (source -> value), or null if the list is null
     */
    @Named("externalIdsToMap")
    default Map<String, Object> externalIdsToMap(
            List<org.solace.scholar_ai.project_service.model.paper.ExternalId> externalIds) {
        if (externalIds == null) return null;
        return externalIds.stream()
                .collect(Collectors.toMap(
                        org.solace.scholar_ai.project_service.model.paper.ExternalId::getSource,
                        org.solace.scholar_ai.project_service.model.paper.ExternalId::getValue));
    }

    /**
     * Helper method to create a PublicationVenue entity from a PaperMetadataDto.
     * Returns null if all venue fields are null.
     *
     * @param dto The PaperMetadataDto containing venue information
     * @return A PublicationVenue entity, or null if no venue data is present
     */
    @Named("mapVenue")
    default org.solace.scholar_ai.project_service.model.paper.PublicationVenue mapVenue(PaperMetadataDto dto) {
        if (dto.venueName() == null
                && dto.publisher() == null
                && dto.volume() == null
                && dto.issue() == null
                && dto.pages() == null) {
            return null;
        }
        return org.solace.scholar_ai.project_service.model.paper.PublicationVenue.builder()
                .venueName(dto.venueName())
                .publisher(dto.publisher())
                .volume(dto.volume())
                .issue(dto.issue())
                .pages(dto.pages())
                .build();
    }

    /**
     * Helper method to create a PaperMetrics entity from a PaperMetadataDto.
     * Returns null if all metrics fields are null.
     *
     * @param dto The PaperMetadataDto containing metrics information
     * @return A PaperMetrics entity, or null if no metrics data is present
     */
    @Named("mapMetrics")
    default org.solace.scholar_ai.project_service.model.paper.PaperMetrics mapMetrics(PaperMetadataDto dto) {
        if (dto.citationCount() == null && dto.referenceCount() == null && dto.influentialCitationCount() == null) {
            return null;
        }
        return org.solace.scholar_ai.project_service.model.paper.PaperMetrics.builder()
                .citationCount(dto.citationCount())
                .referenceCount(dto.referenceCount())
                .influentialCitationCount(dto.influentialCitationCount())
                .build();
    }
}
