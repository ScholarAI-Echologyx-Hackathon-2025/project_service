package org.solace.scholar_ai.project_service.dto.citation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitationCheckResponseDto {

    private UUID id;

    private UUID projectId;

    private UUID documentId;

    private String status; // QUEUED, RUNNING, DONE, ERROR

    private String currentStep;

    private Integer progressPercent; // 0-100

    private String message;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    private CitationSummaryDto summary; // Normalized summary

    private List<CitationIssueDto> issues;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CitationIssueDto {

        private UUID id;

        private UUID projectId;

        private UUID documentId;

        private String filename;

        private String issueType;

        private String severity; // low, medium, high

        private String citationText;

        private Integer position;

        private Integer length;

        private Integer lineStart;

        private Integer lineEnd;

        private String message;

        private List<String> citedKeys;

        private List<SuggestionDto> suggestions;

        private Boolean resolved;

        private String createdAt;

        private List<EvidenceDto> evidence;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SuggestionDto {

            private String kind; // 'local' | 'web'

            private Double score;

            private String paperId;

            private String url;

            private String bibTex;

            private String title;

            private List<String> authors;

            private Integer year;

            private String description;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class EvidenceDto {

            private UUID id;

            private Map<String, Object> source;

            private String matchedText;

            private Double similarity; // 0-1 similarity score

            private Double supportScore; // 0-1 support confidence

            private String extractedContext;
        }
    }
}
