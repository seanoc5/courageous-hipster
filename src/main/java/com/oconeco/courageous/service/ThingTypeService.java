package com.oconeco.courageous.service;

import com.oconeco.courageous.domain.ThingType;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.oconeco.courageous.domain.ThingType}.
 */
public interface ThingTypeService {
    /**
     * Save a thingType.
     *
     * @param thingType the entity to save.
     * @return the persisted entity.
     */
    ThingType save(ThingType thingType);

    /**
     * Updates a thingType.
     *
     * @param thingType the entity to update.
     * @return the persisted entity.
     */
    ThingType update(ThingType thingType);

    /**
     * Partially updates a thingType.
     *
     * @param thingType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ThingType> partialUpdate(ThingType thingType);

    /**
     * Get all the thingTypes.
     *
     * @return the list of entities.
     */
    List<ThingType> findAll();

    /**
     * Get the "id" thingType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ThingType> findOne(Long id);

    /**
     * Delete the "id" thingType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
