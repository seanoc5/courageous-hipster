package com.oconeco.courageous.service;

import com.oconeco.courageous.domain.Search;
import com.oconeco.courageous.domain.SearchResult;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get all the searches with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Search> findAllWithEagerRelationships(Pageable pageable);

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

    /**
     * PERFORM search and download html file
     *
     * @param searchRequest the request to search
     */

    SearchResult performSearch(Search searchRequest);
}
