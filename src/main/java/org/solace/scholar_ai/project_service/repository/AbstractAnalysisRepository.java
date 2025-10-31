package org.solace.scholar_ai.project_service.repository;

import java.util.Optional;
import org.solace.scholar_ai.project_service.model.paper.AbstractAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for AbstractAnalysis entities.
 * Provides methods for querying abstract analysis records associated with papers.
 */
@Repository
public interface AbstractAnalysisRepository extends JpaRepository<AbstractAnalysis, String> {

    /**
     * Finds the most recent active abstract analysis for a paper.
     *
     * @param paperId The ID of the paper
     * @return Optional containing the latest AbstractAnalysis if found
     */
    @Query(
            "SELECT aa FROM AbstractAnalysis aa WHERE aa.paperId = :paperId AND aa.isActive = true ORDER BY aa.updatedAt DESC")
    Optional<AbstractAnalysis> findLatestByPaperId(@Param("paperId") String paperId);

    /**
     * Finds an abstract analysis by paper ID and abstract text hash.
     * Used to check if an analysis already exists for a specific abstract version.
     *
     * @param paperId          The ID of the paper
     * @param abstractTextHash The hash of the abstract text
     * @return Optional containing the AbstractAnalysis if found
     */
    @Query(
            "SELECT aa FROM AbstractAnalysis aa WHERE aa.paperId = :paperId AND aa.abstractTextHash = :abstractTextHash AND aa.isActive = true")
    Optional<AbstractAnalysis> findByPaperIdAndAbstractTextHash(
            @Param("paperId") String paperId, @Param("abstractTextHash") String abstractTextHash);

    /**
     * Checks if an active abstract analysis exists for a paper.
     *
     * @param paperId The ID of the paper
     * @return true if an active analysis exists, false otherwise
     */
    @Query("SELECT COUNT(aa) > 0 FROM AbstractAnalysis aa WHERE aa.paperId = :paperId AND aa.isActive = true")
    boolean existsByPaperId(@Param("paperId") String paperId);

    /**
     * Finds the most recent active abstract analysis for a paper with highlights eagerly fetched.
     *
     * @param paperId The ID of the paper
     * @return Optional containing the latest AbstractAnalysis with highlights if found
     */
    @Query(
            "SELECT aa FROM AbstractAnalysis aa LEFT JOIN FETCH aa.highlights WHERE aa.paperId = :paperId AND aa.isActive = true ORDER BY aa.updatedAt DESC")
    Optional<AbstractAnalysis> findLatestByPaperIdWithHighlights(@Param("paperId") String paperId);
}
