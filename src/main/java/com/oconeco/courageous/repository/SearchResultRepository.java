package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.SearchResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SearchResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {}
