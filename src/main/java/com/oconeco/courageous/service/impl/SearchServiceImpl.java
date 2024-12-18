package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.Content;
import com.oconeco.courageous.domain.Search;
import com.oconeco.courageous.domain.SearchConfiguration;
import com.oconeco.courageous.domain.SearchResult;
import com.oconeco.courageous.repository.ContentRepository;
import com.oconeco.courageous.repository.SearchRepository;
import com.oconeco.courageous.repository.SearchResultRepository;
import com.oconeco.courageous.service.BraveSearchClient;
import com.oconeco.courageous.service.FetcherService;
import com.oconeco.courageous.service.SearchConfigurationService;
import com.oconeco.courageous.service.SearchService;
import com.oconeco.courageous.service.dto.BraveSearchResponseDTO;
import com.oconeco.courageous.service.dto.BraveSearchResultDTO;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing {@link Search}.
 */
@Service
@Transactional
public class SearchServiceImpl implements SearchService {


    private final FetcherService fetcherService;

    private static final Logger LOG = LoggerFactory.getLogger(SearchServiceImpl.class);
    private final SearchConfigurationService searchConfigurationService;
    private final SearchResultRepository searchResultRepository;
    private final ContentRepository contentRepository;
    private final BraveSearchClient braveSearchClient;
    private final SearchRepository searchRepository;

    @Autowired
    public SearchServiceImpl(
        FetcherService fetcherService,
        SearchConfigurationService searchConfigurationService,
        SearchResultRepository searchResultRepository,
        ContentRepository contentRepository,
        SearchRepository searchRepository,
        BraveSearchClient braveSearchClient
    ) {
        this.fetcherService = fetcherService;
        this.searchConfigurationService = searchConfigurationService;
        this.searchResultRepository = searchResultRepository;
        this.contentRepository = contentRepository;
        this.searchRepository = searchRepository;
        this.braveSearchClient = braveSearchClient;
    }

    @Override
    public Search save(Search search) {
        search.setDateCreated(Instant.now());
        search.setLastUpdated(Instant.now());
        search.setActive(true);
        return searchRepository.save(search);
    }

    @Override
    public Search update(Search search) {
        LOG.debug("Request to update Search : {}", search);

        if (search.getId() == null) {
            throw new IllegalArgumentException("Search ID cannot be null for update");
        }
        Optional<Search> existingSearch = searchRepository.findById(search.getId());

        if (existingSearch.isEmpty()) {
            throw new IllegalArgumentException("Search not found with id: " + search.getId());
        }
        if (!existingSearch.get().getActive()) {
            throw new IllegalStateException("Cannot update a soft-deleted (inactive) Search");
        }
        return searchRepository.save(search);
    }

    @Override
    public Optional<Search> partialUpdate(Search search) {
        LOG.debug("Request to partially update Search : {}", search);

        if (search.getId() == null) {
            throw new IllegalArgumentException("Search ID cannot be null for partial update");
        }
        Optional<Search> searchExist = searchRepository.findById(search.getId());

        if (searchExist.isEmpty()) {
            throw new IllegalArgumentException("Search not found with id: " + search.getId());
        }

        if (!searchExist.get().getActive()) {
            throw new IllegalStateException("Cannot update an inactive Search");
        }

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
        return findAllWithEagerRelationships();
    }

    public Page<Search> findAllWithEagerRelationships(Pageable pageable) {
        return searchRepository.findByActiveTrue(pageable);
    }

    public List<Search> findAllWithEagerRelationships() {
        return searchRepository.findByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Search> findOne(Long id) {
        LOG.debug("Request to get Search : {}", id);
        Search search = searchRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Search not found with id: " + id));
        if (!search.getActive()) {
            throw new IllegalStateException("Search with id " + id + " is not active");
        }
        return Optional.of(search);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Search : {}", id);
        Optional<Search> search = searchRepository.findById(id);

        if (search.isPresent()) {
            Search searchData = search.get();
            searchData.setActive(false);
            searchRepository.save(searchData);
            LOG.debug("Search with id {} marked as deleted (soft delete)", id);
        } else {
            throw new EntityNotFoundException("Search with id " + id + " not found");
        }
    }

    /**
     * Retrieve the SearchConfiguration for the current search
     * Send the search request to the Brave Search API
     * Process the search engine response and create Content entities
     * Save the SearchResult and Content entities
     * Trigger asynchronous content fetching
     * To get data from brave client done with  restTemplate-> passed doc Jsoup.parse
     */
    @Override
    public SearchResult performSearch(Search searchRequest) {
        if (searchRequest.getConfigurations().isEmpty()) {
            throw new IllegalArgumentException("Configurations cannot be empty");
        }

        SearchConfiguration config = searchConfigurationService.findById(new ArrayList<>(searchRequest.getConfigurations()).get(0).getId());

        BraveSearchResponseDTO apiResponse = braveSearchClient.search(searchRequest.getQuery(), config);

        List<Content> contents = apiResponse.getResults().stream()
            .map(this::createContent).collect(Collectors.toList());

        SearchResult searchResult = saveSearchResult(searchRequest, apiResponse, contents, config);

        fetcherService.fetchContentForSearch(searchResult);

        return searchResult;
    }

    private Content createContent(BraveSearchResultDTO result) {
        return Content.builder()
            .uri(result.getUrl())
            .title(result.getTitle())
            .description(result.getDescription())
            .favicon(result.getFaviconSrc())
            .dateCreated(Instant.now())
            .lastUpdate(Instant.now())
            .build();
    }

    private SearchResult saveSearchResult(
        Search searchRequest,
        BraveSearchResponseDTO response,
        List<Content> contents,
        SearchConfiguration config
    ) {
        // Save the search entity first, if it is not already saved
        if (searchRequest.getId() == null) {
            searchRequest.setActive(true);
            searchRequest.setAdditionalParams(searchRequest.getAdditionalParams());
            searchRequest.setDateCreated(Instant.now());
            searchRequest.setLastUpdated(Instant.now());
            searchRequest.createdBy(searchRequest.getCreatedBy());
            searchRequest.setConfigurations(searchRequest.getConfigurations());
            searchRequest = searchRepository.save(searchRequest);
        }
        SearchResult searchResult = new SearchResult();
        searchResult.setQuery(searchRequest.getQuery());
        searchResult.setType(response.getType());
        searchResult.setStatusCode(response.getStatusCode());
        searchResult.setDateCreated(Instant.now());
        searchResult.setLastUpdated(Instant.now());
        searchResult.setSearch(searchRequest);
        searchResult.setConfig(config);
        searchResult.setActive(true);

        SearchResult savedResult = searchResultRepository.save(searchResult);

        contents.forEach(content -> {
            content.setSearchResult(savedResult);
            savedResult.getContents().add(content);
        });

        contentRepository.saveAll(contents);

        return searchResultRepository.save(savedResult);
    }
}
