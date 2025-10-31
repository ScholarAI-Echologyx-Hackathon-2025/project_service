package org.solace.scholar_ai.project_service.controller.gap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.solace.scholar_ai.project_service.dto.response.gap.ResearchGapResponse;
import org.solace.scholar_ai.project_service.model.gap.ResearchGap;
import org.solace.scholar_ai.project_service.repository.gap.ResearchGapRepository;
import org.solace.scholar_ai.project_service.service.gap.GapAnalysisMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing individual research gaps.
 * Provides endpoints to retrieve research gaps by various criteria including
 * gap analysis ID, paper ID, status, category, difficulty, and confidence
 * scores.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/research-gaps")
@RequiredArgsConstructor
@Tag(name = "Research Gaps", description = "API for managing individual research gaps")
public class ResearchGapController {

        private final ResearchGapRepository researchGapRepository;
        private final GapAnalysisMapper gapAnalysisMapper;

        /**
         * Retrieves a research gap by its ID.
         *
         * @param id The UUID of the research gap
         * @return ResponseEntity containing the research gap
         */
        @GetMapping("/{id}")
        @Operation(summary = "Get research gap", description = "Retrieve research gap by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Research gap found"),
                        @ApiResponse(responseCode = "404", description = "Research gap not found")
        })
        public ResponseEntity<ResearchGapResponse> getResearchGap(
                        @Parameter(description = "Research gap ID") @PathVariable UUID id) {

                ResearchGap researchGap = researchGapRepository
                                .findById(id)
                                .orElseThrow(() -> new RuntimeException("Research gap not found: " + id));

                ResearchGapResponse response = gapAnalysisMapper.toResponse(researchGap);
                return ResponseEntity.ok(response);
        }

        /**
         * Retrieves all research gaps for a specific gap analysis, ordered by order
         * index.
         *
         * @param gapAnalysisId The UUID of the gap analysis
         * @return ResponseEntity containing a list of research gaps
         */
        @GetMapping("/gap-analysis/{gapAnalysisId}")
        @Operation(summary = "Get research gaps by gap analysis", description = "Retrieve all research gaps for a gap analysis")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Research gaps found") })
        public ResponseEntity<List<ResearchGapResponse>> getResearchGapsByGapAnalysisId(
                        @Parameter(description = "Gap analysis ID") @PathVariable UUID gapAnalysisId) {

                List<ResearchGap> researchGaps = researchGapRepository
                                .findByGapAnalysisIdOrderByOrderIndexAsc(gapAnalysisId);
                List<ResearchGapResponse> responses = researchGaps.stream().map(gapAnalysisMapper::toResponse)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(responses);
        }

        /**
         * Retrieves research gaps for a gap analysis with pagination support.
         *
         * @param gapAnalysisId The UUID of the gap analysis
         * @param pageable      Pagination parameters (default page size: 10)
         * @return ResponseEntity containing a paginated list of research gaps
         */
        @GetMapping("/gap-analysis/{gapAnalysisId}/paginated")
        @Operation(summary = "Get research gaps by gap analysis (paginated)", description = "Retrieve research gaps for a gap analysis with pagination")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Research gaps found") })
        public ResponseEntity<Page<ResearchGapResponse>> getResearchGapsByGapAnalysisIdPaginated(
                        @Parameter(description = "Gap analysis ID") @PathVariable UUID gapAnalysisId,
                        @PageableDefault(size = 10) Pageable pageable) {

                Page<ResearchGap> researchGaps = researchGapRepository
                                .findByGapAnalysisIdOrderByOrderIndexAsc(gapAnalysisId, pageable);
                Page<ResearchGapResponse> responses = researchGaps.map(gapAnalysisMapper::toResponse);

                return ResponseEntity.ok(responses);
        }

        /**
         * Retrieves all research gaps associated with a specific paper.
         *
         * @param paperId The UUID of the paper
         * @return ResponseEntity containing a list of research gaps
         */
        @GetMapping("/paper/{paperId}")
        @Operation(summary = "Get research gaps by paper", description = "Retrieve all research gaps for a paper")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Research gaps found") })
        public ResponseEntity<List<ResearchGapResponse>> getResearchGapsByPaperId(
                        @Parameter(description = "Paper ID") @PathVariable UUID paperId) {

                List<ResearchGap> researchGaps = researchGapRepository.findByPaperId(paperId);
                List<ResearchGapResponse> responses = researchGaps.stream().map(gapAnalysisMapper::toResponse)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(responses);
        }

        /**
         * Retrieves research gaps for a paper with pagination support.
         *
         * @param paperId  The UUID of the paper
         * @param pageable Pagination parameters (default page size: 10)
         * @return ResponseEntity containing a paginated list of research gaps
         */
        @GetMapping("/paper/{paperId}/paginated")
        @Operation(summary = "Get research gaps by paper (paginated)", description = "Retrieve research gaps for a paper with pagination")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Research gaps found") })
        public ResponseEntity<Page<ResearchGapResponse>> getResearchGapsByPaperIdPaginated(
                        @Parameter(description = "Paper ID") @PathVariable UUID paperId,
                        @PageableDefault(size = 10) Pageable pageable) {

                Page<ResearchGap> researchGaps = researchGapRepository.findByPaperId(paperId, pageable);
                Page<ResearchGapResponse> responses = researchGaps.map(gapAnalysisMapper::toResponse);

                return ResponseEntity.ok(responses);
        }

        /**
         * Retrieves only valid research gaps for a specific paper.
         *
         * @param paperId The UUID of the paper
         * @return ResponseEntity containing a list of valid research gaps
         */
        @GetMapping("/paper/{paperId}/valid")
        @Operation(summary = "Get valid research gaps by paper", description = "Retrieve only valid research gaps for a paper")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Valid research gaps found") })
        public ResponseEntity<List<ResearchGapResponse>> getValidResearchGapsByPaperId(
                        @Parameter(description = "Paper ID") @PathVariable UUID paperId) {

                List<ResearchGap> researchGaps = researchGapRepository.findValidGapsByPaperId(paperId);
                List<ResearchGapResponse> responses = researchGaps.stream().map(gapAnalysisMapper::toResponse)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(responses);
        }

        /**
         * Retrieves research gaps filtered by validation status, ordered by creation
         * date descending.
         *
         * @param status The validation status to filter by
         * @return ResponseEntity containing a list of research gaps with the specified
         *         status
         */
        @GetMapping("/status/{status}")
        @Operation(summary = "Get research gaps by status", description = "Retrieve research gaps by validation status")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Research gaps found") })
        public ResponseEntity<List<ResearchGapResponse>> getResearchGapsByStatus(
                        @Parameter(description = "Validation status") @PathVariable ResearchGap.GapValidationStatus status) {

                List<ResearchGap> researchGaps = researchGapRepository
                                .findByValidationStatusOrderByCreatedAtDesc(status);
                List<ResearchGapResponse> responses = researchGaps.stream().map(gapAnalysisMapper::toResponse)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(responses);
        }

        /**
         * Retrieves research gaps filtered by category, ordered by creation date
         * descending.
         *
         * @param category The category to filter by
         * @return ResponseEntity containing a list of research gaps in the specified
         *         category
         */
        @GetMapping("/category/{category}")
        @Operation(summary = "Get research gaps by category", description = "Retrieve research gaps by category")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Research gaps found") })
        public ResponseEntity<List<ResearchGapResponse>> getResearchGapsByCategory(
                        @Parameter(description = "Gap category") @PathVariable String category) {

                List<ResearchGap> researchGaps = researchGapRepository.findByCategoryOrderByCreatedAtDesc(category);
                List<ResearchGapResponse> responses = researchGaps.stream().map(gapAnalysisMapper::toResponse)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(responses);
        }

        /**
         * Retrieves research gaps filtered by estimated difficulty, ordered by creation
         * date descending.
         *
         * @param difficulty The difficulty level to filter by
         * @return ResponseEntity containing a list of research gaps with the specified
         *         difficulty
         */
        @GetMapping("/difficulty/{difficulty}")
        @Operation(summary = "Get research gaps by difficulty", description = "Retrieve research gaps by estimated difficulty")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Research gaps found") })
        public ResponseEntity<List<ResearchGapResponse>> getResearchGapsByDifficulty(
                        @Parameter(description = "Estimated difficulty") @PathVariable String difficulty) {

                List<ResearchGap> researchGaps = researchGapRepository
                                .findByEstimatedDifficultyOrderByCreatedAtDesc(difficulty);
                List<ResearchGapResponse> responses = researchGaps.stream().map(gapAnalysisMapper::toResponse)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(responses);
        }

        /**
         * Retrieves research gaps with confidence scores above the specified minimum
         * threshold.
         *
         * @param minConfidence The minimum confidence score threshold (default: 0.8)
         * @return ResponseEntity containing a list of high-confidence research gaps
         */
        @GetMapping("/high-confidence")
        @Operation(summary = "Get high confidence research gaps", description = "Retrieve research gaps with high confidence scores")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "High confidence research gaps found") })
        public ResponseEntity<List<ResearchGapResponse>> getHighConfidenceResearchGaps(
                        @Parameter(description = "Minimum confidence score") @RequestParam(defaultValue = "0.8") Double minConfidence) {

                List<ResearchGap> researchGaps = researchGapRepository.findHighConfidenceGaps(minConfidence);
                List<ResearchGapResponse> responses = researchGaps.stream().map(gapAnalysisMapper::toResponse)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(responses);
        }
}
