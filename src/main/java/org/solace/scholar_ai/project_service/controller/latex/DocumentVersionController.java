package org.solace.scholar_ai.project_service.controller.latex;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.solace.scholar_ai.project_service.dto.latex.DocumentVersionDTO;
import org.solace.scholar_ai.project_service.dto.response.APIResponse;
import org.solace.scholar_ai.project_service.service.latex.DocumentVersionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing document versions.
 * Provides endpoints to create versions, retrieve version history,
 * and navigate between versions (latest, previous, next).
 */
@RestController
@RequestMapping("/api/document-versions")
@RequiredArgsConstructor
@Slf4j
public class DocumentVersionController {

    private final DocumentVersionService documentVersionService;

    /**
     * Creates a new version snapshot of a document.
     *
     * @param documentId  The UUID of the document
     * @param commitMessage The commit message for this version
     * @param content     The document content to snapshot
     * @param createdBy   Optional UUID of the user creating the version
     * @return ResponseEntity containing the created version
     */
    @PostMapping("/{documentId}/versions")
    public ResponseEntity<APIResponse<DocumentVersionDTO>> createVersion(
            @PathVariable UUID documentId,
            @RequestParam String commitMessage,
            @RequestParam String content,
            @RequestParam(required = false) UUID createdBy) {

        try {
            DocumentVersionDTO version =
                    documentVersionService.createVersion(documentId, content, commitMessage, createdBy);
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Version created successfully")
                    .data(version)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Failed to create version for document {}: {}", documentId, e.getMessage());
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to create version: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves the complete version history for a document.
     *
     * @param documentId The UUID of the document
     * @return ResponseEntity containing a list of all versions
     */
    @GetMapping("/{documentId}/versions")
    public ResponseEntity<APIResponse<List<DocumentVersionDTO>>> getVersionHistory(@PathVariable UUID documentId) {
        try {
            List<DocumentVersionDTO> versions = documentVersionService.getVersionHistory(documentId);
            APIResponse<List<DocumentVersionDTO>> response = APIResponse.<List<DocumentVersionDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Version history retrieved successfully")
                    .data(versions)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to get version history for document {}: {}", documentId, e.getMessage());
            APIResponse<List<DocumentVersionDTO>> response = APIResponse.<List<DocumentVersionDTO>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to get version history: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves a specific version of a document by version number.
     *
     * @param documentId    The UUID of the document
     * @param versionNumber The version number to retrieve
     * @return ResponseEntity containing the document version, or 404 if not found
     */
    @GetMapping("/{documentId}/versions/{versionNumber}")
    public ResponseEntity<APIResponse<DocumentVersionDTO>> getVersion(
            @PathVariable UUID documentId, @PathVariable Integer versionNumber) {

        try {
            Optional<DocumentVersionDTO> version = documentVersionService.getVersion(documentId, versionNumber);
            if (version.isPresent()) {
                APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Version retrieved successfully")
                        .data(version.get())
                        .build();
                return ResponseEntity.ok(response);
            } else {
                APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Version not found")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Failed to get version {} for document {}: {}", versionNumber, documentId, e.getMessage());
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to get version: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves the latest version of a document.
     *
     * @param documentId The UUID of the document
     * @return ResponseEntity containing the latest version, or 404 if no versions exist
     */
    @GetMapping("/{documentId}/versions/latest")
    public ResponseEntity<APIResponse<DocumentVersionDTO>> getLatestVersion(@PathVariable UUID documentId) {
        try {
            Optional<DocumentVersionDTO> version = documentVersionService.getLatestVersion(documentId);
            if (version.isPresent()) {
                APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Latest version retrieved successfully")
                        .data(version.get())
                        .build();
                return ResponseEntity.ok(response);
            } else {
                APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("No versions found")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Failed to get latest version for document {}: {}", documentId, e.getMessage());
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to get latest version: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves the previous version relative to the specified version number.
     *
     * @param documentId    The UUID of the document
     * @param versionNumber The reference version number
     * @return ResponseEntity containing the previous version, or 404 if none exists
     */
    @GetMapping("/{documentId}/versions/{versionNumber}/previous")
    public ResponseEntity<APIResponse<DocumentVersionDTO>> getPreviousVersion(
            @PathVariable UUID documentId, @PathVariable Integer versionNumber) {

        try {
            Optional<DocumentVersionDTO> version = documentVersionService.getPreviousVersion(documentId, versionNumber);
            if (version.isPresent()) {
                APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Previous version retrieved successfully")
                        .data(version.get())
                        .build();
                return ResponseEntity.ok(response);
            } else {
                APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("No previous version found")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error(
                    "Failed to get previous version for document {} version {}: {}",
                    documentId,
                    versionNumber,
                    e.getMessage());
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to get previous version: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves the next version relative to the specified version number.
     *
     * @param documentId    The UUID of the document
     * @param versionNumber The reference version number
     * @return ResponseEntity containing the next version, or 404 if none exists
     */
    @GetMapping("/{documentId}/versions/{versionNumber}/next")
    public ResponseEntity<APIResponse<DocumentVersionDTO>> getNextVersion(
            @PathVariable UUID documentId, @PathVariable Integer versionNumber) {

        try {
            Optional<DocumentVersionDTO> version = documentVersionService.getNextVersion(documentId, versionNumber);
            if (version.isPresent()) {
                APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Next version retrieved successfully")
                        .data(version.get())
                        .build();
                return ResponseEntity.ok(response);
            } else {
                APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("No next version found")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error(
                    "Failed to get next version for document {} version {}: {}",
                    documentId,
                    versionNumber,
                    e.getMessage());
            APIResponse<DocumentVersionDTO> response = APIResponse.<DocumentVersionDTO>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to get next version: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
