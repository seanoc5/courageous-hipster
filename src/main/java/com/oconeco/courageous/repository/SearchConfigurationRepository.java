package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.SearchConfiguration;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

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
}
