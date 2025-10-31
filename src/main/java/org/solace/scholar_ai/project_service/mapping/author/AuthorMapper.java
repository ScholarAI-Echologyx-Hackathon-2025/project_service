package org.solace.scholar_ai.project_service.mapping.author;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.solace.scholar_ai.project_service.dto.author.AuthorDto;
import org.solace.scholar_ai.project_service.model.author.Author;

/**
 * MapStruct mapper interface for converting between Author entity and AuthorDto.
 * Handles JSON string conversions for complex fields like affiliations,
 * research areas, and publication data.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorMapper {

    /** Singleton instance of the mapper. */
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    /**
     * Converts an Author entity to an AuthorDto.
     * Automatically converts JSON string fields to lists and objects.
     *
     * @param author The Author entity to convert
     * @return The corresponding AuthorDto
     */
    @Mapping(target = "allAffiliations", source = "allAffiliations", qualifiedByName = "jsonStringToList")
    @Mapping(target = "researchAreas", source = "researchAreas", qualifiedByName = "jsonStringToList")
    @Mapping(target = "recentPublications", source = "recentPublications", qualifiedByName = "jsonStringToObjectList")
    @Mapping(target = "dataSources", source = "dataSources", qualifiedByName = "jsonStringToList")
    @Mapping(target = "sourcesAttempted", source = "sourcesAttempted", qualifiedByName = "jsonStringToList")
    @Mapping(target = "sourcesSuccessful", source = "sourcesSuccessful", qualifiedByName = "jsonStringToList")
    AuthorDto toDto(Author author);

    /**
     * Converts an AuthorDto to an Author entity.
     * Automatically converts lists and objects to JSON string fields.
     *
     * @param authorDto The AuthorDto to convert
     * @return The corresponding Author entity
     */
    @Mapping(target = "allAffiliations", source = "allAffiliations", qualifiedByName = "listToJsonString")
    @Mapping(target = "researchAreas", source = "researchAreas", qualifiedByName = "listToJsonString")
    @Mapping(target = "recentPublications", source = "recentPublications", qualifiedByName = "objectListToJsonString")
    @Mapping(target = "dataSources", source = "dataSources", qualifiedByName = "listToJsonString")
    @Mapping(target = "sourcesAttempted", source = "sourcesAttempted", qualifiedByName = "listToJsonString")
    @Mapping(target = "sourcesSuccessful", source = "sourcesSuccessful", qualifiedByName = "listToJsonString")
    Author toEntity(AuthorDto authorDto);

    /**
     * Helper method to convert a JSON string to a list of strings.
     * Returns an empty list if the JSON string is null, empty, or invalid.
     *
     * @param jsonString The JSON string to convert
     * @return A list of strings, or an empty list if conversion fails
     */
    @Named("jsonStringToList")
    default List<String> jsonStringToList(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Helper method to convert a JSON string to a list of objects.
     * Returns an empty list if the JSON string is null, empty, or invalid.
     *
     * @param jsonString The JSON string to convert
     * @return A list of objects, or an empty list if conversion fails
     */
    @Named("jsonStringToObjectList")
    default List<Object> jsonStringToObjectList(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, new TypeReference<List<Object>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Helper method to convert a JSON string to an object.
     * Returns null if the JSON string is null, empty, or invalid.
     *
     * @param jsonString The JSON string to convert
     * @return An object, or null if conversion fails
     */
    @Named("jsonStringToObject")
    default Object jsonStringToObject(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, Object.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Helper method to convert a list of strings to a JSON string.
     * Returns null if the list is null or empty.
     *
     * @param list The list of strings to convert
     * @return A JSON string representation, or null if conversion fails
     */
    @Named("listToJsonString")
    default String listToJsonString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Helper method to convert a list of objects to a JSON string.
     * Returns null if the list is null or empty.
     *
     * @param list The list of objects to convert
     * @return A JSON string representation, or null if conversion fails
     */
    @Named("objectListToJsonString")
    default String objectListToJsonString(List<Object> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Helper method to convert an object to a JSON string.
     * Returns null if the object is null.
     *
     * @param object The object to convert
     * @return A JSON string representation, or null if conversion fails
     */
    @Named("objectToJsonString")
    default String objectToJsonString(Object object) {
        if (object == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
