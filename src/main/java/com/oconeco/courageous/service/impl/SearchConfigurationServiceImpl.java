package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.SearchConfiguration;
import com.oconeco.courageous.repository.SearchConfigurationRepository;
import com.oconeco.courageous.service.SearchConfigurationService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return searchConfigurationRepository.save(searchConfiguration);
    }

    @Override
    public SearchConfiguration update(SearchConfiguration searchConfiguration) {
        LOG.debug("Request to update SearchConfiguration : {}", searchConfiguration);
        return searchConfigurationRepository.save(searchConfiguration);
    }

    @Override
    public Optional<SearchConfiguration> partialUpdate(SearchConfiguration searchConfiguration) {
        LOG.debug("Request to partially update SearchConfiguration : {}", searchConfiguration);

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

                return existingSearchConfiguration;
            })
            .map(searchConfigurationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchConfiguration> findAll() {
        LOG.debug("Request to get all SearchConfigurations");
        return searchConfigurationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SearchConfiguration> findOne(Long id) {
        LOG.debug("Request to get SearchConfiguration : {}", id);
        return searchConfigurationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SearchConfiguration : {}", id);
        searchConfigurationRepository.deleteById(id);
    }
}
