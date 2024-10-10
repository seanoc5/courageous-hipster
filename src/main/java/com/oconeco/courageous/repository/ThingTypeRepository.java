package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.ThingType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ThingType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThingTypeRepository extends JpaRepository<ThingType, Long> {}
