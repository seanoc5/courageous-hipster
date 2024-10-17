package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.ContentFragment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContentFragment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentFragmentRepository extends JpaRepository<ContentFragment, Long> {}
