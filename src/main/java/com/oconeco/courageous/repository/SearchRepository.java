package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.Search;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Search entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {
    @Query("select search from Search search where search.createdBy.login = ?#{authentication.name}")
    List<Search> findByCreatedByIsCurrentUser();
}
