package org.solace.scholar_ai.project_service.controller.project;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.solace.scholar_ai.project_service.dto.project.CreateProjectDto;
import org.solace.scholar_ai.project_service.dto.project.ProjectDto;
import org.solace.scholar_ai.project_service.dto.project.UpdateProjectDto;
import org.solace.scholar_ai.project_service.dto.response.APIResponse;
import org.solace.scholar_ai.project_service.model.project.Project;
import org.solace.scholar_ai.project_service.service.project.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing research projects.
 * Provides endpoints for project CRUD operations, status management,
 * starring, statistics, and updating project metrics.
 */
@Slf4j
@RestController
@RequestMapping("api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Creates a new research project.
     *
     * @param createProjectDto The project creation data
     * @return ResponseEntity containing the created project
     */
    @PostMapping
    public ResponseEntity<APIResponse<ProjectDto>> createProject(
            @Valid @RequestBody CreateProjectDto createProjectDto) {
        try {
            log.info("Create project endpoint hit for user: {}", createProjectDto.userId());

            ProjectDto createdProject = projectService.createProject(createProjectDto, createProjectDto.userId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(APIResponse.success(
                            HttpStatus.CREATED.value(), "Project created successfully", createdProject));
        } catch (RuntimeException e) {
            log.error("Error creating project: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.error(
                            HttpStatus.BAD_REQUEST.value(), "Failed to create project: " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error creating project: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create project", null));
        }
    }

    /**
     * Retrieves a project by its ID.
     *
     * @param projectId The UUID of the project
     * @param userId    The UUID of the user
     * @return ResponseEntity containing the project
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<APIResponse<ProjectDto>> getProjectById(
            @PathVariable UUID projectId, @RequestParam UUID userId) {
        try {
            log.info("Get project {} endpoint hit for user: {}", projectId, userId);

            ProjectDto project = projectService.getProjectById(projectId, userId);

            return ResponseEntity.ok(
                    APIResponse.success(HttpStatus.OK.value(), "Project retrieved successfully", project));
        } catch (RuntimeException e) {
            log.error("Error retrieving project {}: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error retrieving project {}: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve project", null));
        }
    }

    /**
     * Retrieves all projects for a user.
     *
     * @param userId The UUID of the user
     * @return ResponseEntity containing a list of projects
     */
    @GetMapping
    public ResponseEntity<APIResponse<List<ProjectDto>>> getAllProjects(@RequestParam UUID userId) {
        try {
            log.info("Get all projects endpoint hit for user: {}", userId);

            List<ProjectDto> projects = projectService.getProjectsByUserId(userId);

            return ResponseEntity.ok(
                    APIResponse.success(HttpStatus.OK.value(), "Projects retrieved successfully", projects));
        } catch (RuntimeException e) {
            log.error("Error retrieving projects: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error retrieving projects: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve projects", null));
        }
    }

    /**
     * Retrieves all projects for a user filtered by status.
     *
     * @param status The project status (ACTIVE, PAUSED, COMPLETED, ARCHIVED)
     * @param userId The UUID of the user
     * @return ResponseEntity containing a list of projects with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<APIResponse<List<ProjectDto>>> getProjectsByStatus(
            @PathVariable String status, @RequestParam UUID userId) {
        try {
            log.info("Get projects by status {} endpoint hit for user: {}", status, userId);

            Project.Status projectStatus = Project.Status.valueOf(status.toUpperCase());
            List<ProjectDto> projects = projectService.getProjectsByUserIdAndStatus(userId, projectStatus);

            return ResponseEntity.ok(
                    APIResponse.success(HttpStatus.OK.value(), "Projects retrieved successfully", projects));
        } catch (IllegalArgumentException e) {
            log.error("Invalid status: {}", status);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.error(HttpStatus.BAD_REQUEST.value(), "Invalid status: " + status, null));
        } catch (RuntimeException e) {
            log.error("Error retrieving projects by status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error retrieving projects by status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve projects", null));
        }
    }

    /**
     * Retrieves all starred projects for a user.
     *
     * @param userId The UUID of the user
     * @return ResponseEntity containing a list of starred projects
     */
    @GetMapping("/starred")
    public ResponseEntity<APIResponse<List<ProjectDto>>> getStarredProjects(@RequestParam UUID userId) {
        try {
            log.info("Get starred projects endpoint hit for user: {}", userId);

            List<ProjectDto> projects = projectService.getStarredProjects(userId);

            return ResponseEntity.ok(
                    APIResponse.success(HttpStatus.OK.value(), "Starred projects retrieved successfully", projects));
        } catch (RuntimeException e) {
            log.error("Error retrieving starred projects: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error retrieving starred projects: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve starred projects", null));
        }
    }

    /**
     * Updates an existing project.
     *
     * @param projectId        The UUID of the project
     * @param updateProjectDto The project update data
     * @return ResponseEntity containing the updated project
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<APIResponse<ProjectDto>> updateProject(
            @PathVariable UUID projectId, @Valid @RequestBody UpdateProjectDto updateProjectDto) {
        try {
            log.info("Update project {} endpoint hit for user: {}", projectId, updateProjectDto.userId());

            ProjectDto updatedProject =
                    projectService.updateProject(projectId, updateProjectDto, updateProjectDto.userId());

            return ResponseEntity.ok(
                    APIResponse.success(HttpStatus.OK.value(), "Project updated successfully", updatedProject));
        } catch (RuntimeException e) {
            log.error("Error updating project {}: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error updating project {}: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update project", null));
        }
    }

    /**
     * Deletes a project.
     *
     * @param projectId The UUID of the project
     * @param userId    The UUID of the user
     * @return ResponseEntity confirming deletion
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<APIResponse<String>> deleteProject(@PathVariable UUID projectId, @RequestParam UUID userId) {
        try {
            log.info("Delete project {} endpoint hit for user: {}", projectId, userId);

            projectService.deleteProject(projectId, userId);

            return ResponseEntity.ok(APIResponse.success(HttpStatus.OK.value(), "Project deleted successfully", null));
        } catch (RuntimeException e) {
            log.error("Error deleting project {}: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error deleting project {}: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete project", null));
        }
    }

    /**
     * Toggles the starred status of a project.
     *
     * @param projectId The UUID of the project
     * @param userId    The UUID of the user
     * @return ResponseEntity containing the updated project
     */
    @PostMapping("/{projectId}/toggle-star")
    public ResponseEntity<APIResponse<ProjectDto>> toggleProjectStar(
            @PathVariable UUID projectId, @RequestParam UUID userId) {
        try {
            log.info("Toggle star for project {} endpoint hit for user: {}", projectId, userId);

            ProjectDto updatedProject = projectService.toggleProjectStar(projectId, userId);

            return ResponseEntity.ok(APIResponse.success(
                    HttpStatus.OK.value(), "Project star status updated successfully", updatedProject));
        } catch (RuntimeException e) {
            log.error("Error toggling star for project {}: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error toggling star for project {}: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update project star status", null));
        }
    }

    /**
     * Retrieves project statistics for a user including counts by status.
     *
     * @param userId The UUID of the user
     * @return ResponseEntity containing project statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<APIResponse<Object>> getProjectStats(@RequestParam UUID userId) {
        try {
            log.info("Get project stats endpoint hit for user: {}", userId);

            long activeCount = projectService.getProjectCountByStatus(userId, Project.Status.ACTIVE);
            long pausedCount = projectService.getProjectCountByStatus(userId, Project.Status.PAUSED);
            long completedCount = projectService.getProjectCountByStatus(userId, Project.Status.COMPLETED);
            long archivedCount = projectService.getProjectCountByStatus(userId, Project.Status.ARCHIVED);

            Object stats = new Object() {
                public final long active = activeCount;
                public final long paused = pausedCount;
                public final long completed = completedCount;
                public final long archived = archivedCount;
                public final long total = activeCount + pausedCount + completedCount + archivedCount;
            };

            return ResponseEntity.ok(
                    APIResponse.success(HttpStatus.OK.value(), "Project statistics retrieved successfully", stats));
        } catch (RuntimeException e) {
            log.error("Error retrieving project stats: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error retrieving project stats: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve project statistics", null));
        }
    }

    /**
     * Updates the total paper count for a project.
     *
     * @param projectId   The UUID of the project
     * @param totalPapers The new total number of papers
     * @return ResponseEntity confirming the update
     */
    @PutMapping("/{projectId}/paper-count")
    public ResponseEntity<APIResponse<String>> updateProjectPaperCount(
            @PathVariable UUID projectId, @RequestParam int totalPapers) {
        try {
            log.info("Update paper count for project {} to {} endpoint hit", projectId, totalPapers);

            projectService.updateProjectPaperCount(projectId, totalPapers);

            return ResponseEntity.ok(
                    APIResponse.success(HttpStatus.OK.value(), "Project paper count updated successfully", null));
        } catch (RuntimeException e) {
            log.error("Error updating paper count for project {}: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error updating paper count for project {}: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update project paper count", null));
        }
    }

    /**
     * Updates the active tasks count for a project.
     *
     * @param projectId  The UUID of the project
     * @param activeTasks The new active tasks count
     * @return ResponseEntity confirming the update
     */
    @PutMapping("/{projectId}/active-tasks")
    public ResponseEntity<APIResponse<String>> updateProjectActiveTasksCount(
            @PathVariable UUID projectId, @RequestParam int activeTasks) {
        try {
            log.info("Update active tasks count for project {} to {} endpoint hit", projectId, activeTasks);

            projectService.updateProjectActiveTasksCount(projectId, activeTasks);

            return ResponseEntity.ok(APIResponse.success(
                    HttpStatus.OK.value(), "Project active tasks count updated successfully", null));
        } catch (RuntimeException e) {
            log.error("Error updating active tasks count for project {}: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        } catch (Exception e) {
            log.error("Unexpected error updating active tasks count for project {}: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Failed to update project active tasks count",
                            null));
        }
    }
}
