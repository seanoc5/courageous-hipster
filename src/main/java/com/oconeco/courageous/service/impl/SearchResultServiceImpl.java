package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.SearchResult;
import com.oconeco.courageous.repository.SearchResultRepository;
import com.oconeco.courageous.service.SearchResultService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


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
        searchResult.setActive(true);
        return searchResultRepository.save(searchResult);
    }

    @Override
    public SearchResult update(SearchResult searchResult) {
        LOG.debug("Request to update SearchResult : {}", searchResult);
        if (searchResult.getId() == null) {
            throw new IllegalArgumentException("SearchResult ID cannot be null for update");
        }
        Optional<SearchResult> existingResult = searchResultRepository.findById(searchResult.getId());

        if (existingResult.isEmpty()) {
            throw new IllegalArgumentException("SearchResult not found with id: " + searchResult.getId());
        }
        if (!existingResult.get().getActive()) {
            throw new IllegalStateException("Cannot update a soft-deleted (inactive) SearchResult");
        }
        return searchResultRepository.save(searchResult);
    }

    @Override
    public Optional<SearchResult> partialUpdate(SearchResult searchResult) {
        LOG.debug("Request to partially update SearchResult : {}", searchResult);

        if (searchResult.getId() == null) {
            throw new IllegalArgumentException("SearchResult ID cannot be null for partial update");
        }
        Optional<SearchResult> existingResult = searchResultRepository.findById(searchResult.getId());

        if (existingResult.isEmpty()) {
            throw new IllegalArgumentException("SearchResult not found with id: " + searchResult.getId());
        }

        if (!existingResult.get().getActive()) {
            throw new IllegalStateException("Cannot update an inactive SearchResult");
        }
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
        return searchResultRepository.findByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SearchResult> findOne(Long id) {
        LOG.debug("Request to get SearchResult : {}", id);

        SearchResult searchResult = searchResultRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("SearchResult not found with id: " + id));
        if (!searchResult.getActive()) {
            throw new IllegalStateException("SearchResult with id " + id + " is not active");
        }
        return Optional.of(searchResult);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SearchResult : {}", id);
        Optional<SearchResult> searchResult = searchResultRepository.findById(id);
        if (searchResult.isPresent()) {
            SearchResult result = searchResult.get();
            result.setActive(false);
            searchResultRepository.save(result);
            LOG.debug("SearchResult with id {} marked as deleted (soft delete)", id);
        } else {
            throw new EntityNotFoundException("SearchResult with id " + id + " not found");
        }
    }
}
