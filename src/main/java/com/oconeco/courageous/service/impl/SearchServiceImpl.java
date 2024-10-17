package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.Search;
import com.oconeco.courageous.repository.SearchRepository;
import com.oconeco.courageous.service.SearchService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oconeco.courageous.domain.Search}.
 */
@Service
@Transactional
public class SearchServiceImpl implements SearchService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final SearchRepository searchRepository;

    public SearchServiceImpl(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public Search save(Search search) {
        LOG.debug("Request to save Search : {}", search);
        return searchRepository.save(search);
    }

    @Override
    public Search update(Search search) {
        LOG.debug("Request to update Search : {}", search);
        return searchRepository.save(search);
    }

    @Override
    public Optional<Search> partialUpdate(Search search) {
        LOG.debug("Request to partially update Search : {}", search);

        return searchRepository
            .findById(search.getId())
            .map(existingSearch -> {
                if (search.getQuery() != null) {
                    existingSearch.setQuery(search.getQuery());
                }
                if (search.getAdditionalParams() != null) {
                    existingSearch.setAdditionalParams(search.getAdditionalParams());
                }
                if (search.getDateCreated() != null) {
                    existingSearch.setDateCreated(search.getDateCreated());
                }
                if (search.getLastUpdated() != null) {
                    existingSearch.setLastUpdated(search.getLastUpdated());
                }

                return existingSearch;
            })
            .map(searchRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Search> findAll() {
        LOG.debug("Request to get all Searches");
        return searchRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Search> findOne(Long id) {
        LOG.debug("Request to get Search : {}", id);
        return searchRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Search : {}", id);
        searchRepository.deleteById(id);
    }
}
