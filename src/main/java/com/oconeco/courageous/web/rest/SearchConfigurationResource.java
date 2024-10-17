package com.oconeco.courageous.web.rest;

import com.oconeco.courageous.domain.SearchConfiguration;
import com.oconeco.courageous.repository.SearchConfigurationRepository;
import com.oconeco.courageous.service.SearchConfigurationService;
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
 * REST controller for managing {@link com.oconeco.courageous.domain.SearchConfiguration}.
 */
@RestController
@RequestMapping("/api/search-configurations")
public class SearchConfigurationResource {

    private static final Logger LOG = LoggerFactory.getLogger(SearchConfigurationResource.class);

    private static final String ENTITY_NAME = "searchConfiguration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SearchConfigurationService searchConfigurationService;

    private final SearchConfigurationRepository searchConfigurationRepository;

    public SearchConfigurationResource(
        SearchConfigurationService searchConfigurationService,
        SearchConfigurationRepository searchConfigurationRepository
    ) {
        this.searchConfigurationService = searchConfigurationService;
        this.searchConfigurationRepository = searchConfigurationRepository;
    }

    /**
     * {@code POST  /search-configurations} : Create a new searchConfiguration.
     *
     * @param searchConfiguration the searchConfiguration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new searchConfiguration, or with status {@code 400 (Bad Request)} if the searchConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SearchConfiguration> createSearchConfiguration(@Valid @RequestBody SearchConfiguration searchConfiguration)
        throws URISyntaxException {
        LOG.debug("REST request to save SearchConfiguration : {}", searchConfiguration);
        if (searchConfiguration.getId() != null) {
            throw new BadRequestAlertException("A new searchConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        searchConfiguration = searchConfigurationService.save(searchConfiguration);
        return ResponseEntity.created(new URI("/api/search-configurations/" + searchConfiguration.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, searchConfiguration.getId().toString()))
            .body(searchConfiguration);
    }

    /**
     * {@code PUT  /search-configurations/:id} : Updates an existing searchConfiguration.
     *
     * @param id the id of the searchConfiguration to save.
     * @param searchConfiguration the searchConfiguration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchConfiguration,
     * or with status {@code 400 (Bad Request)} if the searchConfiguration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the searchConfiguration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SearchConfiguration> updateSearchConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SearchConfiguration searchConfiguration
    ) throws URISyntaxException {
        LOG.debug("REST request to update SearchConfiguration : {}, {}", id, searchConfiguration);
        if (searchConfiguration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchConfiguration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        searchConfiguration = searchConfigurationService.update(searchConfiguration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, searchConfiguration.getId().toString()))
            .body(searchConfiguration);
    }

    /**
     * {@code PATCH  /search-configurations/:id} : Partial updates given fields of an existing searchConfiguration, field will ignore if it is null
     *
     * @param id the id of the searchConfiguration to save.
     * @param searchConfiguration the searchConfiguration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchConfiguration,
     * or with status {@code 400 (Bad Request)} if the searchConfiguration is not valid,
     * or with status {@code 404 (Not Found)} if the searchConfiguration is not found,
     * or with status {@code 500 (Internal Server Error)} if the searchConfiguration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SearchConfiguration> partialUpdateSearchConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SearchConfiguration searchConfiguration
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SearchConfiguration partially : {}, {}", id, searchConfiguration);
        if (searchConfiguration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchConfiguration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SearchConfiguration> result = searchConfigurationService.partialUpdate(searchConfiguration);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, searchConfiguration.getId().toString())
        );
    }

    /**
     * {@code GET  /search-configurations} : get all the searchConfigurations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of searchConfigurations in body.
     */
    @GetMapping("")
    public List<SearchConfiguration> getAllSearchConfigurations() {
        LOG.debug("REST request to get all SearchConfigurations");
        return searchConfigurationService.findAll();
    }

    /**
     * {@code GET  /search-configurations/:id} : get the "id" searchConfiguration.
     *
     * @param id the id of the searchConfiguration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the searchConfiguration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SearchConfiguration> getSearchConfiguration(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SearchConfiguration : {}", id);
        Optional<SearchConfiguration> searchConfiguration = searchConfigurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchConfiguration);
    }

    /**
     * {@code DELETE  /search-configurations/:id} : delete the "id" searchConfiguration.
     *
     * @param id the id of the searchConfiguration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearchConfiguration(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SearchConfiguration : {}", id);
        searchConfigurationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
