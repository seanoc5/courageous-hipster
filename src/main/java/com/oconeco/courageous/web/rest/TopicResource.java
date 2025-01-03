package com.oconeco.courageous.web.rest;

import com.oconeco.courageous.domain.Topic;
import com.oconeco.courageous.repository.TopicRepository;
import com.oconeco.courageous.service.TopicService;
import com.oconeco.courageous.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.oconeco.courageous.domain.Topic}.
 */
@RestController
@RequestMapping("/api/topics")
public class TopicResource {

    private static final Logger LOG = LoggerFactory.getLogger(TopicResource.class);

    private static final String ENTITY_NAME = "topic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TopicService topicService;

    private final TopicRepository topicRepository;

    public TopicResource(TopicService topicService, TopicRepository topicRepository) {
        this.topicService = topicService;
        this.topicRepository = topicRepository;
    }

    /**
     * {@code POST  /topics} : Create a new topic.
     *
     * @param topic the topic to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new topic, or with status {@code 400 (Bad Request)} if the topic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) throws URISyntaxException {
        LOG.debug("REST request to save Topic : {}", topic);
        if (topic.getId() != null) {
            throw new BadRequestAlertException("A new topic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        topic = topicService.save(topic);
        return ResponseEntity.created(new URI("/api/topics/" + topic.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, topic.getId().toString()))
            .body(topic);
    }

    /**
     * {@code PUT  /topics/:id} : Updates an existing topic.
     *
     * @param id the id of the topic to save.
     * @param topic the topic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topic,
     * or with status {@code 400 (Bad Request)} if the topic is not valid,
     * or with status {@code 500 (Internal Server Error)} if the topic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Topic> updateTopic(@PathVariable(value = "id", required = false) final Long id, @RequestBody Topic topic)
        throws URISyntaxException {
        LOG.debug("REST request to update Topic : {}, {}", id, topic);
        if (topic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, topic.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!topicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        topic = topicService.update(topic);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, topic.getId().toString()))
            .body(topic);
    }

    /**
     * {@code PATCH  /topics/:id} : Partial updates given fields of an existing topic, field will ignore if it is null
     *
     * @param id the id of the topic to save.
     * @param topic the topic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topic,
     * or with status {@code 400 (Bad Request)} if the topic is not valid,
     * or with status {@code 404 (Not Found)} if the topic is not found,
     * or with status {@code 500 (Internal Server Error)} if the topic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Topic> partialUpdateTopic(@PathVariable(value = "id", required = false) final Long id, @RequestBody Topic topic)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Topic partially : {}, {}", id, topic);
        if (topic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, topic.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!topicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Topic> result = topicService.partialUpdate(topic);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, topic.getId().toString())
        );
    }

    /**
     * {@code GET  /topics} : get all the topics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of topics in body.
     */
    @GetMapping("")
    public List<Topic> getAllTopics() {
        LOG.debug("REST request to get all Topics");
        return topicService.findAll();
    }

    /**
     * {@code GET  /topics/:id} : get the "id" topic.
     *
     * @param id the id of the topic to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the topic, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopic(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Topic : {}", id);
        Optional<Topic> topic = topicService.findOne(id);
        return ResponseUtil.wrapOrNotFound(topic);
    }

    /**
     * {@code DELETE  /topics/:id} : delete the "id" topic.
     *
     * @param id the id of the topic to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Topic : {}", id);
        topicService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
