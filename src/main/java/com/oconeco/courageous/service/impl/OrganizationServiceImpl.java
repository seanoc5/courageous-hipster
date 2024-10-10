package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.Organization;
import com.oconeco.courageous.repository.OrganizationRepository;
import com.oconeco.courageous.service.OrganizationService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oconeco.courageous.domain.Organization}.
 */
@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Organization save(Organization organization) {
        LOG.debug("Request to save Organization : {}", organization);
        return organizationRepository.save(organization);
    }

    @Override
    public Organization update(Organization organization) {
        LOG.debug("Request to update Organization : {}", organization);
        return organizationRepository.save(organization);
    }

    @Override
    public Optional<Organization> partialUpdate(Organization organization) {
        LOG.debug("Request to partially update Organization : {}", organization);

        return organizationRepository
            .findById(organization.getId())
            .map(existingOrganization -> {
                if (organization.getName() != null) {
                    existingOrganization.setName(organization.getName());
                }
                if (organization.getUrl() != null) {
                    existingOrganization.setUrl(organization.getUrl());
                }
                if (organization.getDescription() != null) {
                    existingOrganization.setDescription(organization.getDescription());
                }
                if (organization.getDateCreated() != null) {
                    existingOrganization.setDateCreated(organization.getDateCreated());
                }
                if (organization.getLastUpdated() != null) {
                    existingOrganization.setLastUpdated(organization.getLastUpdated());
                }

                return existingOrganization;
            })
            .map(organizationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAll() {
        LOG.debug("Request to get all Organizations");
        return organizationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Organization> findOne(Long id) {
        LOG.debug("Request to get Organization : {}", id);
        return organizationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
    }
}
