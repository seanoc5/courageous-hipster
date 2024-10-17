package com.oconeco.courageous.web.rest;

import com.oconeco.courageous.domain.ContentFragment;
import com.oconeco.courageous.repository.ContentFragmentRepository;
import com.oconeco.courageous.service.ContentFragmentService;
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
 * REST controller for managing {@link com.oconeco.courageous.domain.ContentFragment}.
 */
@RestController
@RequestMapping("/api/content-fragments")
public class ContentFragmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContentFragmentResource.class);

    private static final String ENTITY_NAME = "contentFragment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentFragmentService contentFragmentService;

    private final ContentFragmentRepository contentFragmentRepository;

    public ContentFragmentResource(ContentFragmentService contentFragmentService, ContentFragmentRepository contentFragmentRepository) {
        this.contentFragmentService = contentFragmentService;
        this.contentFragmentRepository = contentFragmentRepository;
    }

    /**
     * {@code POST  /content-fragments} : Create a new contentFragment.
     *
     * @param contentFragment the contentFragment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentFragment, or with status {@code 400 (Bad Request)} if the contentFragment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContentFragment> createContentFragment(@Valid @RequestBody ContentFragment contentFragment)
        throws URISyntaxException {
        LOG.debug("REST request to save ContentFragment : {}", contentFragment);
        if (contentFragment.getId() != null) {
            throw new BadRequestAlertException("A new contentFragment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contentFragment = contentFragmentService.save(contentFragment);
        return ResponseEntity.created(new URI("/api/content-fragments/" + contentFragment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, contentFragment.getId().toString()))
            .body(contentFragment);
    }

    /**
     * {@code PUT  /content-fragments/:id} : Updates an existing contentFragment.
     *
     * @param id the id of the contentFragment to save.
     * @param contentFragment the contentFragment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentFragment,
     * or with status {@code 400 (Bad Request)} if the contentFragment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentFragment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContentFragment> updateContentFragment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContentFragment contentFragment
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContentFragment : {}, {}", id, contentFragment);
        if (contentFragment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentFragment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentFragmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contentFragment = contentFragmentService.update(contentFragment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contentFragment.getId().toString()))
            .body(contentFragment);
    }

    /**
     * {@code PATCH  /content-fragments/:id} : Partial updates given fields of an existing contentFragment, field will ignore if it is null
     *
     * @param id the id of the contentFragment to save.
     * @param contentFragment the contentFragment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentFragment,
     * or with status {@code 400 (Bad Request)} if the contentFragment is not valid,
     * or with status {@code 404 (Not Found)} if the contentFragment is not found,
     * or with status {@code 500 (Internal Server Error)} if the contentFragment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContentFragment> partialUpdateContentFragment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContentFragment contentFragment
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ContentFragment partially : {}, {}", id, contentFragment);
        if (contentFragment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentFragment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentFragmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContentFragment> result = contentFragmentService.partialUpdate(contentFragment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contentFragment.getId().toString())
        );
    }

    /**
     * {@code GET  /content-fragments} : get all the contentFragments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contentFragments in body.
     */
    @GetMapping("")
    public List<ContentFragment> getAllContentFragments() {
        LOG.debug("REST request to get all ContentFragments");
        return contentFragmentService.findAll();
    }

    /**
     * {@code GET  /content-fragments/:id} : get the "id" contentFragment.
     *
     * @param id the id of the contentFragment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentFragment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContentFragment> getContentFragment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContentFragment : {}", id);
        Optional<ContentFragment> contentFragment = contentFragmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contentFragment);
    }

    /**
     * {@code DELETE  /content-fragments/:id} : delete the "id" contentFragment.
     *
     * @param id the id of the contentFragment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContentFragment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContentFragment : {}", id);
        contentFragmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
