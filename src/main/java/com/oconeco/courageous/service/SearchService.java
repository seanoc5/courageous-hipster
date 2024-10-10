package com.oconeco.courageous.service;

import com.oconeco.courageous.domain.Search;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.oconeco.courageous.domain.Search}.
 */
public interface SearchService {
    /**
     * Save a search.
     *
     * @param search the entity to save.
     * @return the persisted entity.
     */
    Search save(Search search);

    /**
     * Updates a search.
     *
     * @param search the entity to update.
     * @return the persisted entity.
     */
    Search update(Search search);

    /**
     * Partially updates a search.
     *
     * @param search the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Search> partialUpdate(Search search);

    /**
     * Get all the searches.
     *
     * @return the list of entities.
     */
    List<Search> findAll();

    /**
     * Get the "id" search.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Search> findOne(Long id);

    /**
     * Delete the "id" search.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
