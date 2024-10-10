package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("select tag from Tag tag where tag.createdBy.login = ?#{authentication.name}")
    List<Tag> findByCreatedByIsCurrentUser();
}
