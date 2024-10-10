package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.Context;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Context entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContextRepository extends JpaRepository<Context, Long> {
    @Query("select context from Context context where context.createdBy.login = ?#{authentication.name}")
    List<Context> findByCreatedByIsCurrentUser();
}
