package org.solace.scholar_ai.project_service.dto.citation;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitationCheckRequestDto {

    private UUID projectId;

    private UUID documentId;

    private List<UUID> selectedPaperIds; 
    private String content; 

    private String filename; 

    private String contentHash; 

    private Boolean forceRecheck; 

    private Options options;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Options {

        @Builder.Default
        private Boolean checkLocal = true; 

        @Builder.Default
        private Boolean checkWeb = true; 

        @Builder.Default
        private Double similarityThreshold = 0.85; 

        @Builder.Default
        private Double plagiarismThreshold = 0.92; 

        @Builder.Default
        private Integer maxEvidencePerIssue = 5; 

        @Builder.Default
        private Boolean strictMode = true; 
    }
}
