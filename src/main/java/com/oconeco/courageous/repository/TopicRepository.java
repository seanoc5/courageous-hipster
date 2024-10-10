package com.oconeco.courageous.repository;

import com.oconeco.courageous.domain.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Topic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    @Query("select topic from Topic topic where topic.createdBy.login = ?#{authentication.name}")
    List<Topic> findByCreatedByIsCurrentUser();
}
