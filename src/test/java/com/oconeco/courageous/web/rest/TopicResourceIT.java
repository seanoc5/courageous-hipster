package com.oconeco.courageous.web.rest;

import static com.oconeco.courageous.domain.TopicAsserts.*;
import static com.oconeco.courageous.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconeco.courageous.IntegrationTest;
import com.oconeco.courageous.domain.Topic;
import com.oconeco.courageous.repository.TopicRepository;
import com.oconeco.courageous.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TopicResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TopicResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEFAULT_TOPIC = false;
    private static final Boolean UPDATED_DEFAULT_TOPIC = true;

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/topics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTopicMockMvc;

    private Topic topic;

    private Topic insertedTopic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topic createEntity() {
        return new Topic()
            .label(DEFAULT_LABEL)
            .description(DEFAULT_DESCRIPTION)
            .defaultTopic(DEFAULT_DEFAULT_TOPIC)
            .level(DEFAULT_LEVEL)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topic createUpdatedEntity() {
        return new Topic()
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .defaultTopic(UPDATED_DEFAULT_TOPIC)
            .level(UPDATED_LEVEL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);
    }

    @BeforeEach
    public void initTest() {
        topic = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTopic != null) {
            topicRepository.delete(insertedTopic);
            insertedTopic = null;
        }
    }

    @Test
    @Transactional
    void createTopic() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Topic
        var returnedTopic = om.readValue(
            restTopicMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(topic)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Topic.class
        );

        // Validate the Topic in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTopicUpdatableFieldsEquals(returnedTopic, getPersistedTopic(returnedTopic));

        insertedTopic = returnedTopic;
    }

    @Test
    @Transactional
    void createTopicWithExistingId() throws Exception {
        // Create the Topic with an existing ID
        topic.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(topic)))
            .andExpect(status().isBadRequest());

        // Validate the Topic in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTopics() throws Exception {
        // Initialize the database
        insertedTopic = topicRepository.saveAndFlush(topic);

        // Get all the topicList
        restTopicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topic.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].defaultTopic").value(hasItem(DEFAULT_DEFAULT_TOPIC.booleanValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getTopic() throws Exception {
        // Initialize the database
        insertedTopic = topicRepository.saveAndFlush(topic);

        // Get the topic
        restTopicMockMvc
            .perform(get(ENTITY_API_URL_ID, topic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topic.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.defaultTopic").value(DEFAULT_DEFAULT_TOPIC.booleanValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTopic() throws Exception {
        // Get the topic
        restTopicMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTopic() throws Exception {
        // Initialize the database
        insertedTopic = topicRepository.saveAndFlush(topic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the topic
        Topic updatedTopic = topicRepository.findById(topic.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTopic are not directly saved in db
        em.detach(updatedTopic);
        updatedTopic
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .defaultTopic(UPDATED_DEFAULT_TOPIC)
            .level(UPDATED_LEVEL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restTopicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTopic.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTopic))
            )
            .andExpect(status().isOk());

        // Validate the Topic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTopicToMatchAllProperties(updatedTopic);
    }

    @Test
    @Transactional
    void putNonExistingTopic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        topic.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopicMockMvc
            .perform(put(ENTITY_API_URL_ID, topic.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(topic)))
            .andExpect(status().isBadRequest());

        // Validate the Topic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTopic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        topic.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(topic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTopic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        topic.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopicMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(topic)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Topic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTopicWithPatch() throws Exception {
        // Initialize the database
        insertedTopic = topicRepository.saveAndFlush(topic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the topic using partial update
        Topic partialUpdatedTopic = new Topic();
        partialUpdatedTopic.setId(topic.getId());

        partialUpdatedTopic
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .defaultTopic(UPDATED_DEFAULT_TOPIC)
            .level(UPDATED_LEVEL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restTopicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTopic))
            )
            .andExpect(status().isOk());

        // Validate the Topic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTopicUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTopic, topic), getPersistedTopic(topic));
    }

    @Test
    @Transactional
    void fullUpdateTopicWithPatch() throws Exception {
        // Initialize the database
        insertedTopic = topicRepository.saveAndFlush(topic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the topic using partial update
        Topic partialUpdatedTopic = new Topic();
        partialUpdatedTopic.setId(topic.getId());

        partialUpdatedTopic
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .defaultTopic(UPDATED_DEFAULT_TOPIC)
            .level(UPDATED_LEVEL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restTopicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTopic))
            )
            .andExpect(status().isOk());

        // Validate the Topic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTopicUpdatableFieldsEquals(partialUpdatedTopic, getPersistedTopic(partialUpdatedTopic));
    }

    @Test
    @Transactional
    void patchNonExistingTopic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        topic.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, topic.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(topic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTopic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        topic.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(topic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTopic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        topic.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopicMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(topic)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Topic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTopic() throws Exception {
        // Initialize the database
        insertedTopic = topicRepository.saveAndFlush(topic);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the topic
        restTopicMockMvc
            .perform(delete(ENTITY_API_URL_ID, topic.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return topicRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Topic getPersistedTopic(Topic topic) {
        return topicRepository.findById(topic.getId()).orElseThrow();
    }

    protected void assertPersistedTopicToMatchAllProperties(Topic expectedTopic) {
        assertTopicAllPropertiesEquals(expectedTopic, getPersistedTopic(expectedTopic));
    }

    protected void assertPersistedTopicToMatchUpdatableProperties(Topic expectedTopic) {
        assertTopicAllUpdatablePropertiesEquals(expectedTopic, getPersistedTopic(expectedTopic));
    }
}
