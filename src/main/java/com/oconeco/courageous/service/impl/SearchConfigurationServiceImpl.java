package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.SearchConfiguration;
import com.oconeco.courageous.repository.SearchConfigurationRepository;
import com.oconeco.courageous.service.SearchConfigurationService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link com.oconeco.courageous.domain.SearchConfiguration}.
 */
@Service
@Transactional
public class SearchConfigurationServiceImpl implements SearchConfigurationService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchConfigurationServiceImpl.class);

    private final SearchConfigurationRepository searchConfigurationRepository;

    public SearchConfigurationServiceImpl(SearchConfigurationRepository searchConfigurationRepository) {
        this.searchConfigurationRepository = searchConfigurationRepository;
    }

    @Override
    public SearchConfiguration save(SearchConfiguration searchConfiguration) {
        LOG.debug("Request to save SearchConfiguration : {}", searchConfiguration);
        searchConfiguration.setActive(true);
        return searchConfigurationRepository.save(searchConfiguration);
    }

    @Override
    public SearchConfiguration update(SearchConfiguration searchConfiguration) {
        LOG.debug("Request to update SearchConfiguration : {}", searchConfiguration);

        if (searchConfiguration.getId() == null) {
            throw new IllegalArgumentException("SearchConfiguration ID cannot be null for update");
        }
        Optional<SearchConfiguration> existingConfig = searchConfigurationRepository.findById(searchConfiguration.getId());

        if (existingConfig.isEmpty()) {
            throw new IllegalArgumentException("SearchConfiguration not found with id: " + searchConfiguration.getId());
        }
        if (!existingConfig.get().isActive()) {
            throw new IllegalStateException("Cannot update a soft-deleted (inactive) SearchConfiguration");
        }
        return searchConfigurationRepository.save(searchConfiguration);
    }


    @Override
    public Optional<SearchConfiguration> partialUpdate(SearchConfiguration searchConfiguration) {
        LOG.debug("Request to partially update SearchConfiguration : {}", searchConfiguration);

        if (searchConfiguration.getId() == null) {
            throw new IllegalArgumentException("SearchConfiguration ID cannot be null for partial update");
        }
        Optional<SearchConfiguration> existingConfig = searchConfigurationRepository.findById(searchConfiguration.getId());

        if (existingConfig.isEmpty()) {
            throw new IllegalArgumentException("SearchConfiguration not found with id: " + searchConfiguration.getId());
        }

        if (!existingConfig.get().isActive()) {
            throw new IllegalStateException("Cannot update an inactive SearchConfiguration");
        }

        return searchConfigurationRepository
            .findById(searchConfiguration.getId())
            .map(existingSearchConfiguration -> {
                if (searchConfiguration.getLabel() != null) {
                    existingSearchConfiguration.setLabel(searchConfiguration.getLabel());
                }
                if (searchConfiguration.getDescription() != null) {
                    existingSearchConfiguration.setDescription(searchConfiguration.getDescription());
                }
                if (searchConfiguration.getDefaultConfig() != null) {
                    existingSearchConfiguration.setDefaultConfig(searchConfiguration.getDefaultConfig());
                }
                if (searchConfiguration.getUrl() != null) {
                    existingSearchConfiguration.setUrl(searchConfiguration.getUrl());
                }
                if (searchConfiguration.getParamsJson() != null) {
                    existingSearchConfiguration.setParamsJson(searchConfiguration.getParamsJson());
                }
                if (searchConfiguration.getHeadersJson() != null) {
                    existingSearchConfiguration.setHeadersJson(searchConfiguration.getHeadersJson());
                }
                if (searchConfiguration.getDateCreated() != null) {
                    existingSearchConfiguration.setDateCreated(searchConfiguration.getDateCreated());
                }
                if (searchConfiguration.getLastUpdated() != null) {
                    existingSearchConfiguration.setLastUpdated(searchConfiguration.getLastUpdated());
                }

                return searchConfigurationRepository.save(existingSearchConfiguration);
            })
            .or(() -> {
                LOG.warn("No SearchConfiguration found with ID {}", searchConfiguration.getId());
                return Optional.empty();
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchConfiguration> findAll() {
        LOG.debug("Request to get all active SearchConfigurations");
        return searchConfigurationRepository.findByActiveTrue();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SearchConfiguration> findOne(Long id) {
        LOG.debug("Request to get SearchConfiguration : {}", id);
        SearchConfiguration searchConfiguration = searchConfigurationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("SearchConfiguration not found with id: " + id));
        if (!searchConfiguration.isActive()) {
            throw new IllegalStateException("SearchConfiguration with id " + id + " is not active");
        }
        return Optional.of(searchConfiguration);
    }


    @Transactional
    public void delete(Long id) {
        LOG.debug("Request to delete SearchConfiguration : {}", id);

        Optional<SearchConfiguration> config = searchConfigurationRepository.findById(id);

        if (config.isPresent()) {
            SearchConfiguration searchConfig = config.get();
            searchConfig.setActive(false);
            searchConfigurationRepository.save(searchConfig);
            LOG.debug("SearchConfiguration with id {} marked as deleted (soft delete)", id);
        } else {
            throw new EntityNotFoundException("SearchConfiguration with id " + id + " not found");
        }
    }


    @Override
    public SearchConfiguration findById(Long id) {
        LOG.debug("Fetching SearchConfiguration with id {} where active is true", id);
        SearchConfiguration searchConfiguration = searchConfigurationRepository.findByIdAndActiveTrue(id);
        if (searchConfiguration == null) {
            throw new EntityNotFoundException("SearchConfiguration not found or is marked as deleted");
        }
        return searchConfiguration;
    }
}
