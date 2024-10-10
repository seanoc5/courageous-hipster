package com.oconeco.courageous.service;

import com.oconeco.courageous.domain.SearchResult;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.oconeco.courageous.domain.SearchResult}.
 */
public interface SearchResultService {
    /**
     * Save a searchResult.
     *
     * @param searchResult the entity to save.
     * @return the persisted entity.
     */
    SearchResult save(SearchResult searchResult);

    /**
     * Updates a searchResult.
     *
     * @param searchResult the entity to update.
     * @return the persisted entity.
     */
    SearchResult update(SearchResult searchResult);

    /**
     * Partially updates a searchResult.
     *
     * @param searchResult the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SearchResult> partialUpdate(SearchResult searchResult);

    /**
     * Get all the searchResults.
     *
     * @return the list of entities.
     */
    List<SearchResult> findAll();

    /**
     * Get the "id" searchResult.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SearchResult> findOne(Long id);

    /**
     * Delete the "id" searchResult.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
