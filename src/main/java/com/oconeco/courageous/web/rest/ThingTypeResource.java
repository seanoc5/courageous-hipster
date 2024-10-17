package com.oconeco.courageous.web.rest;

import com.oconeco.courageous.domain.ThingType;
import com.oconeco.courageous.repository.ThingTypeRepository;
import com.oconeco.courageous.service.ThingTypeService;
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
 * REST controller for managing {@link com.oconeco.courageous.domain.ThingType}.
 */
@RestController
@RequestMapping("/api/thing-types")
public class ThingTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ThingTypeResource.class);

    private static final String ENTITY_NAME = "thingType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ThingTypeService thingTypeService;

    private final ThingTypeRepository thingTypeRepository;

    public ThingTypeResource(ThingTypeService thingTypeService, ThingTypeRepository thingTypeRepository) {
        this.thingTypeService = thingTypeService;
        this.thingTypeRepository = thingTypeRepository;
    }

    /**
     * {@code POST  /thing-types} : Create a new thingType.
     *
     * @param thingType the thingType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new thingType, or with status {@code 400 (Bad Request)} if the thingType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ThingType> createThingType(@Valid @RequestBody ThingType thingType) throws URISyntaxException {
        LOG.debug("REST request to save ThingType : {}", thingType);
        if (thingType.getId() != null) {
            throw new BadRequestAlertException("A new thingType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        thingType = thingTypeService.save(thingType);
        return ResponseEntity.created(new URI("/api/thing-types/" + thingType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, thingType.getId().toString()))
            .body(thingType);
    }

    /**
     * {@code PUT  /thing-types/:id} : Updates an existing thingType.
     *
     * @param id the id of the thingType to save.
     * @param thingType the thingType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated thingType,
     * or with status {@code 400 (Bad Request)} if the thingType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the thingType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ThingType> updateThingType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ThingType thingType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ThingType : {}, {}", id, thingType);
        if (thingType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, thingType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!thingTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        thingType = thingTypeService.update(thingType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, thingType.getId().toString()))
            .body(thingType);
    }

    /**
     * {@code PATCH  /thing-types/:id} : Partial updates given fields of an existing thingType, field will ignore if it is null
     *
     * @param id the id of the thingType to save.
     * @param thingType the thingType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated thingType,
     * or with status {@code 400 (Bad Request)} if the thingType is not valid,
     * or with status {@code 404 (Not Found)} if the thingType is not found,
     * or with status {@code 500 (Internal Server Error)} if the thingType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ThingType> partialUpdateThingType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ThingType thingType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ThingType partially : {}, {}", id, thingType);
        if (thingType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, thingType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!thingTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ThingType> result = thingTypeService.partialUpdate(thingType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, thingType.getId().toString())
        );
    }

    /**
     * {@code GET  /thing-types} : get all the thingTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of thingTypes in body.
     */
    @GetMapping("")
    public List<ThingType> getAllThingTypes() {
        LOG.debug("REST request to get all ThingTypes");
        return thingTypeService.findAll();
    }

    /**
     * {@code GET  /thing-types/:id} : get the "id" thingType.
     *
     * @param id the id of the thingType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the thingType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ThingType> getThingType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ThingType : {}", id);
        Optional<ThingType> thingType = thingTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(thingType);
    }

    /**
     * {@code DELETE  /thing-types/:id} : delete the "id" thingType.
     *
     * @param id the id of the thingType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThingType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ThingType : {}", id);
        thingTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
