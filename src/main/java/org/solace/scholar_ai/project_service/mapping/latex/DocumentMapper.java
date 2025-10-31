package org.solace.scholar_ai.project_service.mapping.latex;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.solace.scholar_ai.project_service.dto.latex.CreateDocumentRequestDTO;
import org.solace.scholar_ai.project_service.dto.latex.DocumentResponseDTO;
import org.solace.scholar_ai.project_service.model.latex.Document;

/**
 * MapStruct mapper interface for converting between Document entity and DTOs.
 * Handles mapping for LaTeX document creation, retrieval, and listing operations.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentMapper {

    /**
     * Converts a CreateDocumentRequestDTO to a Document entity for new document creation.
     * Ignores auto-generated fields like ID, timestamps, and file path.
     *
     * @param dto The CreateDocumentRequestDTO containing document data
     * @return A new Document entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "filePath", ignore = true)
    Document toEntity(CreateDocumentRequestDTO dto);

    /**
     * Converts a Document entity to a DocumentResponseDTO.
     *
     * @param entity The Document entity to convert
     * @return The corresponding DocumentResponseDTO
     */
    DocumentResponseDTO toResponseDTO(Document entity);

    /**
     * Converts a list of Document entities to a list of DocumentResponseDTOs.
     *
     * @param entities The list of Document entities to convert
     * @return A list of corresponding DocumentResponseDTOs
     */
    List<DocumentResponseDTO> toResponseDTOList(List<Document> entities);
}
