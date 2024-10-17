package com.oconeco.courageous.service;

import com.oconeco.courageous.domain.ContentFragment;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.oconeco.courageous.domain.ContentFragment}.
 */
public interface ContentFragmentService {
    /**
     * Save a contentFragment.
     *
     * @param contentFragment the entity to save.
     * @return the persisted entity.
     */
    ContentFragment save(ContentFragment contentFragment);

    /**
     * Updates a contentFragment.
     *
     * @param contentFragment the entity to update.
     * @return the persisted entity.
     */
    ContentFragment update(ContentFragment contentFragment);

    /**
     * Partially updates a contentFragment.
     *
     * @param contentFragment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContentFragment> partialUpdate(ContentFragment contentFragment);

    /**
     * Get all the contentFragments.
     *
     * @return the list of entities.
     */
    List<ContentFragment> findAll();

    /**
     * Get the "id" contentFragment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContentFragment> findOne(Long id);

    /**
     * Delete the "id" contentFragment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
