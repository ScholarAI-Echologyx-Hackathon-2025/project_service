package org.solace.scholar_ai.project_service.service.latex;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.solace.scholar_ai.project_service.dto.latex.CreateDocumentRequestDTO;
import org.solace.scholar_ai.project_service.dto.latex.DocumentResponseDTO;
import org.solace.scholar_ai.project_service.dto.latex.DocumentVersionDTO;
import org.solace.scholar_ai.project_service.dto.latex.UpdateDocumentRequestDTO;
import org.solace.scholar_ai.project_service.mapping.latex.DocumentMapper;
import org.solace.scholar_ai.project_service.model.latex.Document;
import org.solace.scholar_ai.project_service.repository.latex.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing LaTeX documents.
 * Provides operations for document CRUD, version management, auto-save, and document search.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final DocumentVersionService documentVersionService;
    private final LaTeXCompilationService latexCompilationService;
    private final ProfessionalLaTeXService professionalLaTeXService;

    /**
     * Creates a new LaTeX document.
     *
     * @param request The document creation request
     * @return The created document response DTO
     */
    @Transactional
    public DocumentResponseDTO createDocument(CreateDocumentRequestDTO request) {
        Document document = documentMapper.toEntity(request);
        Document savedDocument = documentRepository.save(document);
        return documentMapper.toResponseDTO(savedDocument);
    }

    /**
     * Retrieves all documents for a project, ordered by most recently updated.
     *
     * @param projectId The UUID of the project
     * @return A list of document response DTOs
     */
    public List<DocumentResponseDTO> getDocumentsByProjectId(UUID projectId) {
        List<Document> documents = documentRepository.findByProjectIdOrderByUpdatedAtDesc(projectId);
        return documentMapper.toResponseDTOList(documents);
    }

    /**
     * Retrieves a document by its ID.
     *
     * @param documentId The UUID of the document
     * @return The document response DTO
     * @throws RuntimeException if the document is not found
     */
    public DocumentResponseDTO getDocumentById(UUID documentId) {
        Document document = documentRepository
                .findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));
        return documentMapper.toResponseDTO(document);
    }

    /**
     * Updates an existing document. Creates a version snapshot before updating content.
     *
     * @param request The document update request
     * @return The updated document response DTO
     * @throws RuntimeException if the document is not found
     */
    @Transactional
    public DocumentResponseDTO updateDocument(UpdateDocumentRequestDTO request) {
        Document document = documentRepository
                .findById(request.getDocumentId())
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + request.getDocumentId()));

        if (request.getTitle() != null) {
            document.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            // Create a version before updating the document
            documentVersionService.createVersion(
                    document.getId(), document.getContent(), "Content updated", null // createdBy can be null for now
                    );

            document.setContent(request.getContent());
            // Calculate file size based on content length
            document.setFileSize((long) request.getContent().length());
            // Increment version on content change
            document.setVersion(document.getVersion() + 1);
        }

        Document savedDocument = documentRepository.save(document);
        return documentMapper.toResponseDTO(savedDocument);
    }

    /**
     * Auto-saves document content without creating a version snapshot.
     * Updates file size and last accessed timestamp.
     *
     * @param documentId The UUID of the document
     * @param content    The new document content
     * @return The updated document response DTO
     * @throws RuntimeException if the document is not found
     */
    @Transactional
    public DocumentResponseDTO autoSaveDocument(UUID documentId, String content) {
        Document document = documentRepository
                .findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));

        document.setContent(content);
        document.setFileSize((long) content.length());
        document.setIsAutoSaved(true);
        // Update last accessed time
        document.setLastAccessed(java.time.Instant.now());

        Document savedDocument = documentRepository.save(document);
        return documentMapper.toResponseDTO(savedDocument);
    }

    /**
     * Updates the last accessed timestamp for a document.
     *
     * @param documentId The UUID of the document
     */
    @Transactional
    public void updateLastAccessed(UUID documentId) {
        documentRepository.updateLastAccessed(documentId, java.time.Instant.now());
    }

    /**
     * Creates a new document with a specified filename.
     * Ensures .tex extension, handles duplicate names, and initializes with a template.
     *
     * @param projectId The UUID of the project
     * @param fileName  The desired filename for the document
     * @return The created document response DTO
     */
    public DocumentResponseDTO createDocumentWithName(UUID projectId, String fileName) {
        // Ensure file has .tex extension
        if (!fileName.endsWith(".tex")) {
            fileName = fileName + ".tex";
        }

        // Handle duplicate names by appending a number
        String finalFileName = fileName;
        int counter = 1;

        while (documentRepository
                .findByProjectIdAndTitle(projectId, finalFileName)
                .isPresent()) {
            String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf("."));
            String extension = fileName.substring(fileName.lastIndexOf("."));
            finalFileName = nameWithoutExt + " (" + counter + ")" + extension;
            counter++;
        }

        Document document = Document.builder()
                .projectId(projectId)
                .title(finalFileName)
                .content(
                        "% " + finalFileName
                                + "\n\\documentclass{article}\n\\begin{document}\n\n% Start writing your LaTeX document here...\n\n\\end{document}")
                .documentType(org.solace.scholar_ai.project_service.model.latex.DocumentType.LATEX)
                .fileExtension("tex")
                .fileSize(0L)
                .version(1)
                .isAutoSaved(false)
                .build();

        Document savedDocument = documentRepository.save(document);
        return documentMapper.toResponseDTO(savedDocument);
    }

    /**
     * Searches documents by title within a project.
     *
     * @param projectId The UUID of the project
     * @param query      The search query string
     * @return A list of matching document response DTOs
     */
    public List<DocumentResponseDTO> searchDocuments(UUID projectId, String query) {
        List<Document> documents =
                documentRepository.findByProjectIdAndTitleContainingIgnoreCaseOrderByUpdatedAtDesc(projectId, query);
        return documentMapper.toResponseDTOList(documents);
    }

    /**
     * Gets the total count of documents for a project.
     *
     * @param projectId The UUID of the project
     * @return The number of documents in the project
     */
    public long getDocumentCount(UUID projectId) {
        return documentRepository.countByProjectId(projectId);
    }

    /**
     * Retrieves the version history for a document.
     *
     * @param documentId The UUID of the document
     * @return A list of document version DTOs ordered by version number
     */
    public List<DocumentVersionDTO> getDocumentVersionHistory(UUID documentId) {
        return documentVersionService.getVersionHistory(documentId);
    }

    /**
     * Retrieves a specific version of a document.
     *
     * @param documentId    The UUID of the document
     * @param versionNumber The version number to retrieve
     * @return The document version DTO
     * @throws RuntimeException if the version is not found
     */
    public DocumentVersionDTO getDocumentVersion(UUID documentId, Integer versionNumber) {
        return documentVersionService
                .getVersion(documentId, versionNumber)
                .orElseThrow(() -> new RuntimeException("Version not found"));
    }

    /**
     * Retrieves the previous version of a document relative to the current version.
     *
     * @param documentId     The UUID of the document
     * @param currentVersion The current version number
     * @return The previous document version DTO
     * @throws RuntimeException if no previous version exists
     */
    public DocumentVersionDTO getPreviousVersion(UUID documentId, Integer currentVersion) {
        return documentVersionService
                .getPreviousVersion(documentId, currentVersion)
                .orElseThrow(() -> new RuntimeException("No previous version found"));
    }

    /**
     * Retrieves the next version of a document relative to the current version.
     *
     * @param documentId     The UUID of the document
     * @param currentVersion The current version number
     * @return The next document version DTO
     * @throws RuntimeException if no next version exists
     */
    public DocumentVersionDTO getNextVersion(UUID documentId, Integer currentVersion) {
        return documentVersionService
                .getNextVersion(documentId, currentVersion)
                .orElseThrow(() -> new RuntimeException("No next version found"));
    }

    /**
     * Creates a manual version snapshot of a document.
     *
     * @param documentId    The UUID of the document
     * @param content       The document content to snapshot
     * @param commitMessage The commit message for this version
     * @return The created document version DTO
     */
    public DocumentVersionDTO createManualVersion(UUID documentId, String content, String commitMessage) {
        return documentVersionService.createVersion(documentId, content, commitMessage, null);
    }

    /**
     * Deletes a document from the database.
     *
     * @param documentId The UUID of the document to delete
     * @throws RuntimeException if the document is not found
     */
    @Transactional
    public void deleteDocument(UUID documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new RuntimeException("Document not found with id: " + documentId);
        }
        documentRepository.deleteById(documentId);
    }

    /**
     * Compiles LaTeX content to HTML or PDF.
     * Attempts to use professional LaTeX service first, falls back to basic compilation if it fails.
     *
     * @param latexContent The LaTeX content to compile
     * @return The compiled output (HTML or PDF)
     */
    public String compileLatex(String latexContent) {
        try {
            // Use the new professional LaTeX service
            return professionalLaTeXService.compileLatex(latexContent);
        } catch (Exception e) {
            log.error("Professional LaTeX compilation failed", e);
            // Fallback to old service
            try {
                return latexCompilationService.compileLatexToHtml(latexContent);
            } catch (Exception fallbackException) {
                log.error("Fallback compilation also failed", fallbackException);
                return latexCompilationService.compileLatexFallback(latexContent);
            }
        }
    }
}
