package com.oconeco.courageous.service;

import com.oconeco.courageous.domain.Context;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.oconeco.courageous.domain.Context}.
 */
public interface ContextService {
    /**
     * Save a context.
     *
     * @param context the entity to save.
     * @return the persisted entity.
     */
    Context save(Context context);

    /**
     * Updates a context.
     *
     * @param context the entity to update.
     * @return the persisted entity.
     */
    Context update(Context context);

    /**
     * Partially updates a context.
     *
     * @param context the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Context> partialUpdate(Context context);

    /**
     * Get all the contexts.
     *
     * @return the list of entities.
     */
    List<Context> findAll();

    /**
     * Get the "id" context.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Context> findOne(Long id);

    /**
     * Delete the "id" context.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
