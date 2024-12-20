package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the SearchResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {
    List<SearchResult> findByActiveTrue();

    SearchResult findByIdAndActiveTrue(Long id);
}
