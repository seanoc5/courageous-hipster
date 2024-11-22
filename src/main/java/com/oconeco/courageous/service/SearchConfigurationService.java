package com.oconeco.courageous.service;

import com.oconeco.courageous.domain.SearchConfiguration;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.oconeco.courageous.domain.SearchConfiguration}.
 */
public interface SearchConfigurationService {
    /**
     * Save a searchConfiguration.
     *
     * @param searchConfiguration the entity to save.
     * @return the persisted entity.
     */
    SearchConfiguration save(SearchConfiguration searchConfiguration);

    /**
     * Updates a searchConfiguration.
     *
     * @param searchConfiguration the entity to update.
     * @return the persisted entity.
     */
    SearchConfiguration update(SearchConfiguration searchConfiguration);

    /**
     * Partially updates a searchConfiguration.
     *
     * @param searchConfiguration the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SearchConfiguration> partialUpdate(SearchConfiguration searchConfiguration);

    /**
     * Get all the searchConfigurations.
     *
     * @return the list of entities.
     */
    List<SearchConfiguration> findAll();

    /**
     * Get the "id" searchConfiguration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SearchConfiguration> findOne(Long id);

    /**
     * Delete the "id" searchConfiguration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Find  by "id" searchConfiguration.
     *
     * @param id the label of the entity.
     */
    SearchConfiguration findById(Long id);
}
