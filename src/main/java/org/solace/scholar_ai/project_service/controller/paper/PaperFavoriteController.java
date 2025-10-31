package org.solace.scholar_ai.project_service.controller.paper;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.solace.scholar_ai.project_service.dto.paper.PaperFavoriteRequest;
import org.solace.scholar_ai.project_service.dto.paper.PaperFavoriteResponse;
import org.solace.scholar_ai.project_service.dto.response.APIResponse;
import org.solace.scholar_ai.project_service.service.paper.PaperFavoriteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing paper favorites within projects.
 * Provides endpoints for adding, removing, toggling, and retrieving favorite papers.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/projects/{projectId}/papers/favorites")
@RequiredArgsConstructor
public class PaperFavoriteController {

    private final PaperFavoriteService favoriteService;

    /**
     * Adds a paper to favorites for a project.
     *
     * @param projectId The UUID of the project
     * @param paperId   The UUID of the paper
     * @param request   The favorite request data
     * @param userId    The UUID of the user (from X-User-ID header)
     * @return ResponseEntity containing the favorite response
     */
    @PostMapping("/{paperId}")
    public ResponseEntity<APIResponse<PaperFavoriteResponse>> addToFavorites(
            @PathVariable UUID projectId,
            @PathVariable UUID paperId,
            @RequestBody PaperFavoriteRequest request,
            @RequestHeader("X-User-ID") UUID userId) {

        log.info("Adding paper {} to favorites for project {} by user {}", paperId, projectId, userId);

        try {
            PaperFavoriteResponse response = favoriteService.addToFavorites(projectId, paperId, userId, request);
            return ResponseEntity.ok(APIResponse.success(200, "Paper added to favorites successfully", response));
        } catch (Exception e) {
            log.error("Error adding paper to favorites: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(APIResponse.error(400, "Failed to add paper to favorites: " + e.getMessage(), null));
        }
    }

    /**
     * Removes a paper from favorites for a project.
     *
     * @param projectId The UUID of the project
     * @param paperId   The UUID of the paper
     * @param userId    The UUID of the user (from X-User-ID header)
     * @return ResponseEntity confirming removal
     */
    @DeleteMapping("/{paperId}")
    public ResponseEntity<APIResponse<Void>> removeFromFavorites(
            @PathVariable UUID projectId, @PathVariable UUID paperId, @RequestHeader("X-User-ID") UUID userId) {

        log.info("Removing paper {} from favorites for project {} by user {}", paperId, projectId, userId);

        try {
            favoriteService.removeFromFavorites(projectId, paperId, userId);
            return ResponseEntity.ok(APIResponse.success(200, "Paper removed from favorites successfully", null));
        } catch (Exception e) {
            log.error("Error removing paper from favorites: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(APIResponse.error(400, "Failed to remove paper from favorites: " + e.getMessage(), null));
        }
    }

    /**
     * Toggles the favorite status of a paper (adds if not favorited, removes if favorited).
     *
     * @param projectId The UUID of the project
     * @param paperId   The UUID of the paper
     * @param request   The favorite request data
     * @param userId    The UUID of the user (from X-User-ID header)
     * @return ResponseEntity containing the favorite response or null if removed
     */
    @PostMapping("/{paperId}/toggle")
    public ResponseEntity<APIResponse<PaperFavoriteResponse>> toggleFavorite(
            @PathVariable UUID projectId,
            @PathVariable UUID paperId,
            @RequestBody PaperFavoriteRequest request,
            @RequestHeader("X-User-ID") UUID userId) {

        log.info("Toggling favorite status for paper {} in project {} by user {}", paperId, projectId, userId);

        try {
            PaperFavoriteResponse response = favoriteService.toggleFavorite(projectId, paperId, userId, request);
            if (response == null) {
                return ResponseEntity.ok(APIResponse.success(200, "Paper removed from favorites successfully", null));
            } else {
                return ResponseEntity.ok(APIResponse.success(200, "Paper added to favorites successfully", response));
            }
        } catch (Exception e) {
            log.error("Error toggling favorite: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(APIResponse.error(400, "Failed to toggle favorite: " + e.getMessage(), null));
        }
    }

    /**
     * Retrieves all favorite papers for a project.
     *
     * @param projectId The UUID of the project
     * @param userId    The UUID of the user (from X-User-ID header)
     * @return ResponseEntity containing a list of favorite papers
     */
    @GetMapping
    public ResponseEntity<APIResponse<List<PaperFavoriteResponse>>> getFavorites(
            @PathVariable UUID projectId, @RequestHeader("X-User-ID") UUID userId) {

        log.info("Getting favorites for project {} by user {}", projectId, userId);

        try {
            List<PaperFavoriteResponse> favorites = favoriteService.getFavorites(projectId, userId);
            return ResponseEntity.ok(APIResponse.success(200, "Favorites retrieved successfully", favorites));
        } catch (Exception e) {
            log.error("Error getting favorites: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(APIResponse.error(400, "Failed to get favorites: " + e.getMessage(), null));
        }
    }

    /**
     * Retrieves favorite papers for a project with pagination support.
     *
     * @param projectId The UUID of the project
     * @param userId    The UUID of the user (from X-User-ID header)
     * @param pageable  Pagination parameters
     * @return ResponseEntity containing a paginated list of favorite papers
     */
    @GetMapping("/paginated")
    public ResponseEntity<APIResponse<Page<PaperFavoriteResponse>>> getFavoritesPaginated(
            @PathVariable UUID projectId, @RequestHeader("X-User-ID") UUID userId, Pageable pageable) {

        log.info("Getting paginated favorites for project {} by user {}", projectId, userId);

        try {
            Page<PaperFavoriteResponse> favorites = favoriteService.getFavoritesPaginated(projectId, userId, pageable);
            return ResponseEntity.ok(APIResponse.success(200, "Favorites retrieved successfully", favorites));
        } catch (Exception e) {
            log.error("Error getting paginated favorites: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(APIResponse.error(400, "Failed to get favorites: " + e.getMessage(), null));
        }
    }

    /**
     * Gets the count of favorite papers for a project.
     *
     * @param projectId The UUID of the project
     * @param userId    The UUID of the user (from X-User-ID header)
     * @return ResponseEntity containing the favorite count
     */
    @GetMapping("/count")
    public ResponseEntity<APIResponse<Long>> getFavoriteCount(
            @PathVariable UUID projectId, @RequestHeader("X-User-ID") UUID userId) {

        log.info("Getting favorite count for project {} by user {}", projectId, userId);

        try {
            long count = favoriteService.getFavoriteCount(projectId, userId);
            return ResponseEntity.ok(APIResponse.success(200, "Favorite count retrieved successfully", count));
        } catch (Exception e) {
            log.error("Error getting favorite count: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(APIResponse.error(400, "Failed to get favorite count: " + e.getMessage(), null));
        }
    }

    /**
     * Checks if a paper is favorited in a project.
     *
     * @param projectId The UUID of the project
     * @param paperId   The UUID of the paper
     * @param userId    The UUID of the user (from X-User-ID header)
     * @return ResponseEntity containing true if favorited, false otherwise
     */
    @GetMapping("/{paperId}/status")
    public ResponseEntity<APIResponse<Boolean>> isFavorited(
            @PathVariable UUID projectId, @PathVariable UUID paperId, @RequestHeader("X-User-ID") UUID userId) {

        log.info("Checking if paper {} is favorited in project {} by user {}", paperId, projectId, userId);

        try {
            boolean isFavorited = favoriteService.isFavorited(projectId, paperId, userId);
            return ResponseEntity.ok(APIResponse.success(200, "Favorite status retrieved successfully", isFavorited));
        } catch (Exception e) {
            log.error("Error checking favorite status: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(APIResponse.error(400, "Failed to check favorite status: " + e.getMessage(), null));
        }
    }

    /**
     * Updates favorite information for a paper in a project.
     *
     * @param projectId The UUID of the project
     * @param paperId   The UUID of the paper
     * @param request   The favorite update request data
     * @param userId    The UUID of the user (from X-User-ID header)
     * @return ResponseEntity containing the updated favorite response
     */
    @PutMapping("/{paperId}")
    public ResponseEntity<APIResponse<PaperFavoriteResponse>> updateFavorite(
            @PathVariable UUID projectId,
            @PathVariable UUID paperId,
            @RequestBody PaperFavoriteRequest request,
            @RequestHeader("X-User-ID") UUID userId) {

        log.info("Updating favorite for paper {} in project {} by user {}", paperId, projectId, userId);

        try {
            PaperFavoriteResponse response = favoriteService.updateFavorite(projectId, paperId, userId, request);
            return ResponseEntity.ok(APIResponse.success(200, "Favorite updated successfully", response));
        } catch (Exception e) {
            log.error("Error updating favorite: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(APIResponse.error(400, "Failed to update favorite: " + e.getMessage(), null));
        }
    }
}
