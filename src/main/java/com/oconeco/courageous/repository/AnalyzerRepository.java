package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.Analyzer;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Analyzer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalyzerRepository extends JpaRepository<Analyzer, Long> {
    @Query("select analyzer from Analyzer analyzer where analyzer.createdBy.login = ?#{authentication.name}")
    List<Analyzer> findByCreatedByIsCurrentUser();
}
