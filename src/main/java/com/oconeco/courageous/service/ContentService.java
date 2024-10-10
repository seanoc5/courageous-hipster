package com.oconeco.courageous.service;

import com.oconeco.courageous.domain.Content;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.oconeco.courageous.domain.Content}.
 */
public interface ContentService {
    /**
     * Save a content.
     *
     * @param content the entity to save.
     * @return the persisted entity.
     */
    Content save(Content content);

    /**
     * Updates a content.
     *
     * @param content the entity to update.
     * @return the persisted entity.
     */
    Content update(Content content);

    /**
     * Partially updates a content.
     *
     * @param content the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Content> partialUpdate(Content content);

    /**
     * Get all the contents.
     *
     * @return the list of entities.
     */
    List<Content> findAll();

    /**
     * Get the "id" content.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Content> findOne(Long id);

    /**
     * Delete the "id" content.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
