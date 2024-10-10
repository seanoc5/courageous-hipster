package com.oconeco.courageous.web.rest;

import com.oconeco.courageous.domain.SearchResult;
import com.oconeco.courageous.repository.SearchResultRepository;
import com.oconeco.courageous.service.SearchResultService;
import com.oconeco.courageous.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.oconeco.courageous.domain.SearchResult}.
 */
@RestController
@RequestMapping("/api/search-results")
public class SearchResultResource {

    private static final Logger LOG = LoggerFactory.getLogger(SearchResultResource.class);

    private static final String ENTITY_NAME = "searchResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SearchResultService searchResultService;

    private final SearchResultRepository searchResultRepository;

    public SearchResultResource(SearchResultService searchResultService, SearchResultRepository searchResultRepository) {
        this.searchResultService = searchResultService;
        this.searchResultRepository = searchResultRepository;
    }

    /**
     * {@code POST  /search-results} : Create a new searchResult.
     *
     * @param searchResult the searchResult to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new searchResult, or with status {@code 400 (Bad Request)} if the searchResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SearchResult> createSearchResult(@Valid @RequestBody SearchResult searchResult) throws URISyntaxException {
        LOG.debug("REST request to save SearchResult : {}", searchResult);
        if (searchResult.getId() != null) {
            throw new BadRequestAlertException("A new searchResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        searchResult = searchResultService.save(searchResult);
        return ResponseEntity.created(new URI("/api/search-results/" + searchResult.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, searchResult.getId().toString()))
            .body(searchResult);
    }

    /**
     * {@code PUT  /search-results/:id} : Updates an existing searchResult.
     *
     * @param id the id of the searchResult to save.
     * @param searchResult the searchResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchResult,
     * or with status {@code 400 (Bad Request)} if the searchResult is not valid,
     * or with status {@code 500 (Internal Server Error)} if the searchResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SearchResult> updateSearchResult(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SearchResult searchResult
    ) throws URISyntaxException {
        LOG.debug("REST request to update SearchResult : {}, {}", id, searchResult);
        if (searchResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchResult.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        searchResult = searchResultService.update(searchResult);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, searchResult.getId().toString()))
            .body(searchResult);
    }

    /**
     * {@code PATCH  /search-results/:id} : Partial updates given fields of an existing searchResult, field will ignore if it is null
     *
     * @param id the id of the searchResult to save.
     * @param searchResult the searchResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchResult,
     * or with status {@code 400 (Bad Request)} if the searchResult is not valid,
     * or with status {@code 404 (Not Found)} if the searchResult is not found,
     * or with status {@code 500 (Internal Server Error)} if the searchResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SearchResult> partialUpdateSearchResult(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SearchResult searchResult
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SearchResult partially : {}, {}", id, searchResult);
        if (searchResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchResult.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SearchResult> result = searchResultService.partialUpdate(searchResult);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, searchResult.getId().toString())
        );
    }

    /**
     * {@code GET  /search-results} : get all the searchResults.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of searchResults in body.
     */
    @GetMapping("")
    public List<SearchResult> getAllSearchResults() {
        LOG.debug("REST request to get all SearchResults");
        return searchResultService.findAll();
    }

    /**
     * {@code GET  /search-results/:id} : get the "id" searchResult.
     *
     * @param id the id of the searchResult to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the searchResult, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SearchResult> getSearchResult(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SearchResult : {}", id);
        Optional<SearchResult> searchResult = searchResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchResult);
    }

    /**
     * {@code DELETE  /search-results/:id} : delete the "id" searchResult.
     *
     * @param id the id of the searchResult to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearchResult(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SearchResult : {}", id);
        searchResultService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
