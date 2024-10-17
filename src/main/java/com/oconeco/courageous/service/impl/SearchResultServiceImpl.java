package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.SearchResult;
import com.oconeco.courageous.repository.SearchResultRepository;
import com.oconeco.courageous.service.SearchResultService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oconeco.courageous.domain.SearchResult}.
 */
@Service
@Transactional
public class SearchResultServiceImpl implements SearchResultService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchResultServiceImpl.class);

    private final SearchResultRepository searchResultRepository;

    public SearchResultServiceImpl(SearchResultRepository searchResultRepository) {
        this.searchResultRepository = searchResultRepository;
    }

    @Override
    public SearchResult save(SearchResult searchResult) {
        LOG.debug("Request to save SearchResult : {}", searchResult);
        return searchResultRepository.save(searchResult);
    }

    @Override
    public SearchResult update(SearchResult searchResult) {
        LOG.debug("Request to update SearchResult : {}", searchResult);
        return searchResultRepository.save(searchResult);
    }

    @Override
    public Optional<SearchResult> partialUpdate(SearchResult searchResult) {
        LOG.debug("Request to partially update SearchResult : {}", searchResult);

        return searchResultRepository
            .findById(searchResult.getId())
            .map(existingSearchResult -> {
                if (searchResult.getQuery() != null) {
                    existingSearchResult.setQuery(searchResult.getQuery());
                }
                if (searchResult.getType() != null) {
                    existingSearchResult.setType(searchResult.getType());
                }
                if (searchResult.getResponseBody() != null) {
                    existingSearchResult.setResponseBody(searchResult.getResponseBody());
                }
                if (searchResult.getStatusCode() != null) {
                    existingSearchResult.setStatusCode(searchResult.getStatusCode());
                }
                if (searchResult.getDateCreated() != null) {
                    existingSearchResult.setDateCreated(searchResult.getDateCreated());
                }
                if (searchResult.getLastUpdated() != null) {
                    existingSearchResult.setLastUpdated(searchResult.getLastUpdated());
                }

                return existingSearchResult;
            })
            .map(searchResultRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchResult> findAll() {
        LOG.debug("Request to get all SearchResults");
        return searchResultRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SearchResult> findOne(Long id) {
        LOG.debug("Request to get SearchResult : {}", id);
        return searchResultRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SearchResult : {}", id);
        searchResultRepository.deleteById(id);
    }
}
