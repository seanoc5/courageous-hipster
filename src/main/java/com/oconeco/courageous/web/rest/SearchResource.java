package com.oconeco.courageous.web.rest;

import com.oconeco.courageous.domain.Search;
import com.oconeco.courageous.repository.SearchRepository;
import com.oconeco.courageous.service.SearchService;
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
 * REST controller for managing {@link com.oconeco.courageous.domain.Search}.
 */
@RestController
@RequestMapping("/api/searches")
public class SearchResource {

    private static final Logger LOG = LoggerFactory.getLogger(SearchResource.class);

    private static final String ENTITY_NAME = "search";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SearchService searchService;

    private final SearchRepository searchRepository;

    public SearchResource(SearchService searchService, SearchRepository searchRepository) {
        this.searchService = searchService;
        this.searchRepository = searchRepository;
    }

    /**
     * {@code POST  /searches} : Create a new search.
     *
     * @param search the search to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new search, or with status {@code 400 (Bad Request)} if the search has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Search> createSearch(@Valid @RequestBody Search search) throws URISyntaxException {
        LOG.debug("REST request to save Search : {}", search);
        if (search.getId() != null) {
            throw new BadRequestAlertException("A new search cannot already have an ID", ENTITY_NAME, "idexists");
        }
        search = searchService.save(search);
        return ResponseEntity.created(new URI("/api/searches/" + search.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, search.getId().toString()))
            .body(search);
    }

    /**
     * {@code PUT  /searches/:id} : Updates an existing search.
     *
     * @param id the id of the search to save.
     * @param search the search to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated search,
     * or with status {@code 400 (Bad Request)} if the search is not valid,
     * or with status {@code 500 (Internal Server Error)} if the search couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Search> updateSearch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Search search
    ) throws URISyntaxException {
        LOG.debug("REST request to update Search : {}, {}", id, search);
        if (search.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, search.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        search = searchService.update(search);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, search.getId().toString()))
            .body(search);
    }

    /**
     * {@code PATCH  /searches/:id} : Partial updates given fields of an existing search, field will ignore if it is null
     *
     * @param id the id of the search to save.
     * @param search the search to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated search,
     * or with status {@code 400 (Bad Request)} if the search is not valid,
     * or with status {@code 404 (Not Found)} if the search is not found,
     * or with status {@code 500 (Internal Server Error)} if the search couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Search> partialUpdateSearch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Search search
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Search partially : {}, {}", id, search);
        if (search.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, search.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Search> result = searchService.partialUpdate(search);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, search.getId().toString())
        );
    }

    /**
     * {@code GET  /searches} : get all the searches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of searches in body.
     */
    @GetMapping("")
    public List<Search> getAllSearches() {
        LOG.debug("REST request to get all Searches");
        return searchService.findAll();
    }

    /**
     * {@code GET  /searches/:id} : get the "id" search.
     *
     * @param id the id of the search to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the search, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Search> getSearch(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Search : {}", id);
        Optional<Search> search = searchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(search);
    }

    /**
     * {@code DELETE  /searches/:id} : delete the "id" search.
     *
     * @param id the id of the search to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearch(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Search : {}", id);
        searchService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
