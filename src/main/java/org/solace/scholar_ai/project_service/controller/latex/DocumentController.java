package org.solace.scholar_ai.project_service.controller.latex;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.solace.scholar_ai.project_service.dto.latex.CompileLatexRequestDTO;
import org.solace.scholar_ai.project_service.dto.latex.CreateDocumentRequestDTO;
import org.solace.scholar_ai.project_service.dto.latex.DocumentResponseDTO;
import org.solace.scholar_ai.project_service.dto.latex.DocumentVersionDTO;
import org.solace.scholar_ai.project_service.dto.latex.UpdateDocumentRequestDTO;
import org.solace.scholar_ai.project_service.dto.response.APIResponse;
import org.solace.scholar_ai.project_service.service.latex.DocumentService;
import org.solace.scholar_ai.project_service.service.latex.LaTeXCompilationService;
import org.solace.scholar_ai.project_service.service.latex.PDFLatexService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing LaTeX documents.
 * Provides endpoints for document CRUD operations, LaTeX compilation,
 * PDF generation, version management, and document search functionality.
 */
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Validated
public class DocumentController {

    private final DocumentService documentService;
    private final LaTeXCompilationService latexCompilationService;
    private final PDFLatexService pdfLatexService;

    /**
     * Creates a new LaTeX document.
     *
     * @param request The document creation request
     * @return ResponseEntity containing the created document
     */
    @PostMapping
    public ResponseEntity<APIResponse<DocumentResponseDTO>> createDocument(
            @Valid @RequestBody CreateDocumentRequestDTO request) {
        DocumentResponseDTO document = documentService.createDocument(request);
        APIResponse<DocumentResponseDTO> response = APIResponse.<DocumentResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Document created successfully")
                .data(document)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves all documents for a specific project.
     *
     * @param projectId The UUID of the project
     * @return ResponseEntity containing a list of documents
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<APIResponse<List<DocumentResponseDTO>>> getDocumentsByProjectId(
            @PathVariable UUID projectId) {
        List<DocumentResponseDTO> documents = documentService.getDocumentsByProjectId(projectId);
        APIResponse<List<DocumentResponseDTO>> response = APIResponse.<List<DocumentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Documents retrieved successfully")
                .data(documents)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a document by its ID.
     *
     * @param documentId The UUID of the document
     * @return ResponseEntity containing the document
     */
    @GetMapping("/{documentId}")
    public ResponseEntity<APIResponse<DocumentResponseDTO>> getDocumentById(@PathVariable UUID documentId) {
        DocumentResponseDTO document = documentService.getDocumentById(documentId);
        APIResponse<DocumentResponseDTO> response = APIResponse.<DocumentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Document retrieved successfully")
                .data(document)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing document.
     *
     * @param request The document update request
     * @return ResponseEntity containing the updated document
     */
    @PutMapping
    public ResponseEntity<APIResponse<DocumentResponseDTO>> updateDocument(
            @Valid @RequestBody UpdateDocumentRequestDTO request) {
        DocumentResponseDTO document = documentService.updateDocument(request);
        APIResponse<DocumentResponseDTO> response = APIResponse.<DocumentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Document updated successfully")
                .data(document)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a document by its ID.
     *
     * @param documentId The UUID of the document to delete
     * @return ResponseEntity confirming deletion
     */
    @DeleteMapping("/{documentId}")
    public ResponseEntity<APIResponse<Void>> deleteDocument(@PathVariable UUID documentId) {
        documentService.deleteDocument(documentId);
        APIResponse<Void> response = APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Document deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Compiles the LaTeX content of a document.
     *
     * @param documentId The UUID of the document to compile
     * @return ResponseEntity containing the compiled content
     */
    @PostMapping("/{documentId}/compile")
    public ResponseEntity<APIResponse<String>> compileDocument(@PathVariable UUID documentId) {
        DocumentResponseDTO document = documentService.getDocumentById(documentId);
        String compiledContent = documentService.compileLatex(document.getContent());
        APIResponse<String> response = APIResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Document compiled successfully")
                .data(compiledContent)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Compiles LaTeX content provided in the request.
     *
     * @param request The compilation request containing LaTeX content
     * @return ResponseEntity containing the compiled content
     */
    @PostMapping("/compile")
    public ResponseEntity<APIResponse<String>> compileLatex(@Valid @RequestBody CompileLatexRequestDTO request) {
        String compiledContent = documentService.compileLatex(request.getLatexContent());
        APIResponse<String> response = APIResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("LaTeX compiled successfully")
                .data(compiledContent)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Compiles LaTeX content to PDF and returns it as a downloadable resource.
     *
     * @param request The compilation request containing LaTeX content
     * @return ResponseEntity containing the PDF resource for download
     */
    @PostMapping("/compile-pdf")
    public ResponseEntity<Resource> compileToPdf(@Valid @RequestBody CompileLatexRequestDTO request) {
        try {
            Resource pdfResource = pdfLatexService.compileLatexToPDF(request.getLatexContent());
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"document.pdf\"")
                    .header("Content-Type", "application/pdf")
                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .header("Pragma", "no-cache")
                    .header("Expires", "0")
                    .body(pdfResource);
        } catch (Exception e) {
            throw new RuntimeException("Failed to compile LaTeX to PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Compiles LaTeX content to PDF and returns it for inline preview.
     *
     * @param request The compilation request containing LaTeX content
     * @return ResponseEntity containing the PDF resource for inline display
     */
    @PostMapping("/preview-pdf")
    public ResponseEntity<Resource> previewPdf(@Valid @RequestBody CompileLatexRequestDTO request) {
        try {
            Resource pdfResource = pdfLatexService.compileLatexToPDF(request.getLatexContent());
            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"preview.pdf\"")
                    .header("Content-Type", "application/pdf")
                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .header("Pragma", "no-cache")
                    .header("Expires", "0")
                    .body(pdfResource);
        } catch (Exception e) {
            throw new RuntimeException("Failed to preview LaTeX PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Generates a PDF from LaTeX content with a custom filename.
     *
     * @param request Map containing "latexContent" and optional "filename"
     * @return ResponseEntity containing the generated PDF resource
     */
    @PostMapping("/generate-pdf")
    public ResponseEntity<Resource> generatePDF(@RequestBody java.util.Map<String, String> request) {
        String latexContent = request.get("latexContent");
        String filename = request.get("filename");

        if (latexContent == null || latexContent.trim().isEmpty()) {
            throw new RuntimeException("LaTeX content is required");
        }

        if (filename == null || filename.trim().isEmpty()) {
            filename = "document";
        }

        return latexCompilationService.generatePDF(latexContent, filename);
    }

    /**
     * Creates a document with a specified name in a project.
     *
     * @param projectId The UUID of the project
     * @param fileName  The name for the new document
     * @return ResponseEntity containing the created document
     */
    @PostMapping("/create-with-name")
    public ResponseEntity<APIResponse<DocumentResponseDTO>> createDocumentWithName(
            @RequestParam UUID projectId, @RequestParam String fileName) {
        try {
            DocumentResponseDTO document = documentService.createDocumentWithName(projectId, fileName);
            APIResponse<DocumentResponseDTO> response = APIResponse.<DocumentResponseDTO>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Document created successfully")
                    .data(document)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            APIResponse<DocumentResponseDTO> response = APIResponse.<DocumentResponseDTO>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to create document: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Auto-saves document content without creating a new version.
     *
     * @param documentId The UUID of the document
     * @param content    The updated document content
     * @return ResponseEntity containing the auto-saved document
     */
    @PostMapping("/{documentId}/auto-save")
    public ResponseEntity<APIResponse<DocumentResponseDTO>> autoSaveDocument(
            @PathVariable UUID documentId, @RequestBody String content) {
        try {
            DocumentResponseDTO document = documentService.autoSaveDocument(documentId, content);
            APIResponse<DocumentResponseDTO> response = APIResponse.<DocumentResponseDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Document auto-saved successfully")
                    .data(document)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<DocumentResponseDTO> response = APIResponse.<DocumentResponseDTO>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to auto-save document: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Updates the last accessed timestamp for a document.
     *
     * @param documentId The UUID of the document
     * @return ResponseEntity confirming the update
     */
    @PostMapping("/{documentId}/access")
    public ResponseEntity<APIResponse<String>> updateLastAccessed(@PathVariable UUID documentId) {
        try {
            documentService.updateLastAccessed(documentId);
            APIResponse<String> response = APIResponse.<String>builder()
                    .status(HttpStatus.OK.value())
                    .message("Last accessed updated successfully")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<String> response = APIResponse.<String>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to update last accessed: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Searches for documents within a project by query string.
     *
     * @param projectId The UUID of the project
     * @param query     The search query string
     * @return ResponseEntity containing a list of matching documents
     */
    @GetMapping("/search")
    public ResponseEntity<APIResponse<List<DocumentResponseDTO>>> searchDocuments(
            @RequestParam UUID projectId, @RequestParam String query) {
        try {
            List<DocumentResponseDTO> documents = documentService.searchDocuments(projectId, query);
            APIResponse<List<DocumentResponseDTO>> response = APIResponse.<List<DocumentResponseDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Documents found successfully")
                    .data(documents)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<List<DocumentResponseDTO>> response = APIResponse.<List<DocumentResponseDTO>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to search documents: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Gets the count of documents in a project.
     *
     * @param projectId The UUID of the project
     * @return ResponseEntity containing the document count
     */
    @GetMapping("/count")
    public ResponseEntity<APIResponse<Long>> getDocumentCount(@RequestParam UUID projectId) {
        try {
            long count = documentService.getDocumentCount(projectId);
            APIResponse<Long> response = APIResponse.<Long>builder()
                    .status(HttpStatus.OK.value())
                    .message("Document count retrieved successfully")
                    .data(count)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<Long> response = APIResponse.<Long>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to get document count: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves the version history for a document.
     *
     * @param documentId The UUID of the document
     * @return ResponseEntity containing a list of document versions
     */
    @GetMapping("/{documentId}/versions")
    public ResponseEntity<APIResponse<List<DocumentVersionDTO>>> getVersionHistory(@PathVariable UUID documentId) {
        try {
            List<DocumentVersionDTO> versions = documentService.getDocumentVersionHistory(documentId);
            APIResponse<List<DocumentVersionDTO>> response = APIResponse.<List<DocumentVersionDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Version history retrieved successfully")
                    .data(versions)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<List<DocumentVersionDTO>> response = APIResponse.<List<DocumentVersionDTO>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to get version history: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Creates a manual version snapshot of a document.
     *
     * @param documentId    The UUID of the document
     * @param commitMessage The commit message for this version
     * @param content       The document content to snapshot
     * @return ResponseEntity containing the created version
     */
    @PostMapping("/{documentId}/versions")
    public ResponseEntity<APIResponse<DocumentVersionDTO>> createVersion(
            @PathVariable UUID documentId, @RequestParam String commitMessage, @RequestParam String content) {
        try {
            DocumentVersionDTO version = documentService.createManualVersion(documentId, content, commitMessage);
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Version created successfully")
                    .data(version)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to create version: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves a specific version of a document by version number.
     *
     * @param documentId    The UUID of the document
     * @param versionNumber The version number to retrieve
     * @return ResponseEntity containing the document version
     */
    @GetMapping("/{documentId}/versions/{versionNumber}")
    public ResponseEntity<APIResponse<DocumentVersionDTO>> getVersion(
            @PathVariable UUID documentId, @PathVariable Integer versionNumber) {
        try {
            DocumentVersionDTO version = documentService.getDocumentVersion(documentId, versionNumber);
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Version retrieved successfully")
                    .data(version)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Version not found: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Retrieves the previous version relative to the specified version number.
     *
     * @param documentId    The UUID of the document
     * @param versionNumber The reference version number
     * @return ResponseEntity containing the previous version
     */
    @GetMapping("/{documentId}/versions/{versionNumber}/previous")
    public ResponseEntity<APIResponse<DocumentVersionDTO>> getPreviousVersion(
            @PathVariable UUID documentId, @PathVariable Integer versionNumber) {
        try {
            DocumentVersionDTO version = documentService.getPreviousVersion(documentId, versionNumber);
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Previous version retrieved successfully")
                    .data(version)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("No previous version found: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Retrieves the next version relative to the specified version number.
     *
     * @param documentId    The UUID of the document
     * @param versionNumber The reference version number
     * @return ResponseEntity containing the next version
     */
    @GetMapping("/{documentId}/versions/{versionNumber}/next")
    public ResponseEntity<APIResponse<DocumentVersionDTO>> getNextVersion(
            @PathVariable UUID documentId, @PathVariable Integer versionNumber) {
        try {
            DocumentVersionDTO version = documentService.getNextVersion(documentId, versionNumber);
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Next version retrieved successfully")
                    .data(version)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("No next version found: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
