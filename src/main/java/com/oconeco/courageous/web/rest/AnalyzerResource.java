package com.oconeco.courageous.web.rest;

import com.oconeco.courageous.domain.Analyzer;
import com.oconeco.courageous.repository.AnalyzerRepository;
import com.oconeco.courageous.service.AnalyzerService;
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
 * REST controller for managing {@link com.oconeco.courageous.domain.Analyzer}.
 */
@RestController
@RequestMapping("/api/analyzers")
public class AnalyzerResource {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyzerResource.class);

    private static final String ENTITY_NAME = "analyzer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnalyzerService analyzerService;

    private final AnalyzerRepository analyzerRepository;

    public AnalyzerResource(AnalyzerService analyzerService, AnalyzerRepository analyzerRepository) {
        this.analyzerService = analyzerService;
        this.analyzerRepository = analyzerRepository;
    }

    /**
     * {@code POST  /analyzers} : Create a new analyzer.
     *
     * @param analyzer the analyzer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new analyzer, or with status {@code 400 (Bad Request)} if the analyzer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Analyzer> createAnalyzer(@Valid @RequestBody Analyzer analyzer) throws URISyntaxException {
        LOG.debug("REST request to save Analyzer : {}", analyzer);
        if (analyzer.getId() != null) {
            throw new BadRequestAlertException("A new analyzer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        analyzer = analyzerService.save(analyzer);
        return ResponseEntity.created(new URI("/api/analyzers/" + analyzer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, analyzer.getId().toString()))
            .body(analyzer);
    }

    /**
     * {@code PUT  /analyzers/:id} : Updates an existing analyzer.
     *
     * @param id the id of the analyzer to save.
     * @param analyzer the analyzer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analyzer,
     * or with status {@code 400 (Bad Request)} if the analyzer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the analyzer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Analyzer> updateAnalyzer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Analyzer analyzer
    ) throws URISyntaxException {
        LOG.debug("REST request to update Analyzer : {}, {}", id, analyzer);
        if (analyzer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analyzer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!analyzerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        analyzer = analyzerService.update(analyzer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, analyzer.getId().toString()))
            .body(analyzer);
    }

    /**
     * {@code PATCH  /analyzers/:id} : Partial updates given fields of an existing analyzer, field will ignore if it is null
     *
     * @param id the id of the analyzer to save.
     * @param analyzer the analyzer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analyzer,
     * or with status {@code 400 (Bad Request)} if the analyzer is not valid,
     * or with status {@code 404 (Not Found)} if the analyzer is not found,
     * or with status {@code 500 (Internal Server Error)} if the analyzer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Analyzer> partialUpdateAnalyzer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Analyzer analyzer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Analyzer partially : {}, {}", id, analyzer);
        if (analyzer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analyzer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!analyzerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Analyzer> result = analyzerService.partialUpdate(analyzer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, analyzer.getId().toString())
        );
    }

    /**
     * {@code GET  /analyzers} : get all the analyzers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of analyzers in body.
     */
    @GetMapping("")
    public List<Analyzer> getAllAnalyzers() {
        LOG.debug("REST request to get all Analyzers");
        return analyzerService.findAll();
    }

    /**
     * {@code GET  /analyzers/:id} : get the "id" analyzer.
     *
     * @param id the id of the analyzer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the analyzer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Analyzer> getAnalyzer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Analyzer : {}", id);
        Optional<Analyzer> analyzer = analyzerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(analyzer);
    }

    /**
     * {@code DELETE  /analyzers/:id} : delete the "id" analyzer.
     *
     * @param id the id of the analyzer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalyzer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Analyzer : {}", id);
        analyzerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
