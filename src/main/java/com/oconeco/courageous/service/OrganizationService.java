package com.oconeco.courageous.service;

import com.oconeco.courageous.domain.Organization;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.oconeco.courageous.domain.Organization}.
 */
public interface OrganizationService {
    /**
     * Save a organization.
     *
     * @param organization the entity to save.
     * @return the persisted entity.
     */
    Organization save(Organization organization);

    /**
     * Updates a organization.
     *
     * @param organization the entity to update.
     * @return the persisted entity.
     */
    Organization update(Organization organization);

    /**
     * Partially updates a organization.
     *
     * @param organization the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Organization> partialUpdate(Organization organization);

    /**
     * Get all the organizations.
     *
     * @return the list of entities.
     */
    List<Organization> findAll();

    /**
     * Get the "id" organization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Organization> findOne(Long id);

    /**
     * Delete the "id" organization.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
