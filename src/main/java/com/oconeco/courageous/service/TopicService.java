package com.oconeco.courageous.service;

import com.oconeco.courageous.domain.Topic;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.oconeco.courageous.domain.Topic}.
 */
public interface TopicService {
    /**
     * Save a topic.
     *
     * @param topic the entity to save.
     * @return the persisted entity.
     */
    Topic save(Topic topic);

    /**
     * Updates a topic.
     *
     * @param topic the entity to update.
     * @return the persisted entity.
     */
    Topic update(Topic topic);

    /**
     * Partially updates a topic.
     *
     * @param topic the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Topic> partialUpdate(Topic topic);

    /**
     * Get all the topics.
     *
     * @return the list of entities.
     */
    List<Topic> findAll();

    /**
     * Get the "id" topic.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Topic> findOne(Long id);

    /**
     * Delete the "id" topic.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
