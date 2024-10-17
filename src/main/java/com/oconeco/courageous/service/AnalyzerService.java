package com.oconeco.courageous.service;

import com.oconeco.courageous.domain.Analyzer;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.oconeco.courageous.domain.Analyzer}.
 */
public interface AnalyzerService {
    /**
     * Save a analyzer.
     *
     * @param analyzer the entity to save.
     * @return the persisted entity.
     */
    Analyzer save(Analyzer analyzer);

    /**
     * Updates a analyzer.
     *
     * @param analyzer the entity to update.
     * @return the persisted entity.
     */
    Analyzer update(Analyzer analyzer);

    /**
     * Partially updates a analyzer.
     *
     * @param analyzer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Analyzer> partialUpdate(Analyzer analyzer);

    /**
     * Get all the analyzers.
     *
     * @return the list of entities.
     */
    List<Analyzer> findAll();

    /**
     * Get the "id" analyzer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Analyzer> findOne(Long id);

    /**
     * Delete the "id" analyzer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
