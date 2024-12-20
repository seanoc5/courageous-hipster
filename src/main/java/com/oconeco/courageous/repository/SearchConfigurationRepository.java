package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.SearchConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the SearchConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchConfigurationRepository extends JpaRepository<SearchConfiguration, Long> {
    @Query(
        "select searchConfiguration from SearchConfiguration searchConfiguration where searchConfiguration.createdBy.login = ?#{authentication.name}"
    )
    List<SearchConfiguration> findByCreatedByIsCurrentUser();

    List<SearchConfiguration> findByActiveTrue();

    SearchConfiguration findByIdAndActiveTrue(Long id);
}
