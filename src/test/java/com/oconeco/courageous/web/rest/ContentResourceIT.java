package com.oconeco.courageous.web.rest;

import static com.oconeco.courageous.domain.ContentAsserts.*;
import static com.oconeco.courageous.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconeco.courageous.IntegrationTest;
import com.oconeco.courageous.domain.Content;
import com.oconeco.courageous.repository.ContentRepository;
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
 * Integration tests for the {@link ContentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContentResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMS = "AAAAAAAAAA";
    private static final String UPDATED_PARAMS = "BBBBBBBBBB";

    private static final String DEFAULT_BODY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_BODY_TEXT = "BBBBBBBBBB";

    private static final Long DEFAULT_TEXT_SIZE = 1L;
    private static final Long UPDATED_TEXT_SIZE = 2L;

    private static final String DEFAULT_STRUCTURED_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_STRUCTURED_CONTENT = "BBBBBBBBBB";

    private static final Long DEFAULT_STRUCTURE_SIZE = 1L;
    private static final Long UPDATED_STRUCTURE_SIZE = 2L;

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBTYPE = "AAAAAAAAAA";
    private static final String UPDATED_SUBTYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MINE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MINE_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_PUBLISH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLISH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OFFENSIVE_FLAG = "AAAAAAAAAA";
    private static final String UPDATED_OFFENSIVE_FLAG = "BBBBBBBBBB";

    private static final String DEFAULT_FAVICON = "AAAAAAAAAA";
    private static final String UPDATED_FAVICON = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/contents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentMockMvc;

    private Content content;

    private Content insertedContent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Content createEntity() {
        return new Content()
            .title(DEFAULT_TITLE)
            .uri(DEFAULT_URI)
            .description(DEFAULT_DESCRIPTION)
            .path(DEFAULT_PATH)
            .source(DEFAULT_SOURCE)
            .params(DEFAULT_PARAMS)
            .bodyText(DEFAULT_BODY_TEXT)
            .textSize(DEFAULT_TEXT_SIZE)
            .structuredContent(DEFAULT_STRUCTURED_CONTENT)
            .structureSize(DEFAULT_STRUCTURE_SIZE)
            .author(DEFAULT_AUTHOR)
            .language(DEFAULT_LANGUAGE)
            .type(DEFAULT_TYPE)
            .subtype(DEFAULT_SUBTYPE)
            .mineType(DEFAULT_MINE_TYPE)
            .publishDate(DEFAULT_PUBLISH_DATE)
            .offensiveFlag(DEFAULT_OFFENSIVE_FLAG)
            .favicon(DEFAULT_FAVICON)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastUpdate(DEFAULT_LAST_UPDATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Content createUpdatedEntity() {
        return new Content()
            .title(UPDATED_TITLE)
            .uri(UPDATED_URI)
            .description(UPDATED_DESCRIPTION)
            .path(UPDATED_PATH)
            .source(UPDATED_SOURCE)
            .params(UPDATED_PARAMS)
            .bodyText(UPDATED_BODY_TEXT)
            .textSize(UPDATED_TEXT_SIZE)
            .structuredContent(UPDATED_STRUCTURED_CONTENT)
            .structureSize(UPDATED_STRUCTURE_SIZE)
            .author(UPDATED_AUTHOR)
            .language(UPDATED_LANGUAGE)
            .type(UPDATED_TYPE)
            .subtype(UPDATED_SUBTYPE)
            .mineType(UPDATED_MINE_TYPE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .offensiveFlag(UPDATED_OFFENSIVE_FLAG)
            .favicon(UPDATED_FAVICON)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdate(UPDATED_LAST_UPDATE);
    }

    @BeforeEach
    public void initTest() {
        content = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedContent != null) {
            contentRepository.delete(insertedContent);
            insertedContent = null;
        }
    }

    @Test
    @Transactional
    void createContent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Content
        var returnedContent = om.readValue(
            restContentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(content)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Content.class
        );

        // Validate the Content in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContentUpdatableFieldsEquals(returnedContent, getPersistedContent(returnedContent));

        insertedContent = returnedContent;
    }

    @Test
    @Transactional
    void createContentWithExistingId() throws Exception {
        // Create the Content with an existing ID
        content.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(content)))
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        content.setTitle(null);

        // Create the Content, which fails.

        restContentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(content)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUriIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        content.setUri(null);

        // Create the Content, which fails.

        restContentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(content)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContents() throws Exception {
        // Initialize the database
        insertedContent = contentRepository.saveAndFlush(content);

        // Get all the contentList
        restContentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(content.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].uri").value(hasItem(DEFAULT_URI)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].params").value(hasItem(DEFAULT_PARAMS)))
            .andExpect(jsonPath("$.[*].bodyText").value(hasItem(DEFAULT_BODY_TEXT.toString())))
            .andExpect(jsonPath("$.[*].textSize").value(hasItem(DEFAULT_TEXT_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].structuredContent").value(hasItem(DEFAULT_STRUCTURED_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].structureSize").value(hasItem(DEFAULT_STRUCTURE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].subtype").value(hasItem(DEFAULT_SUBTYPE)))
            .andExpect(jsonPath("$.[*].mineType").value(hasItem(DEFAULT_MINE_TYPE)))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].offensiveFlag").value(hasItem(DEFAULT_OFFENSIVE_FLAG)))
            .andExpect(jsonPath("$.[*].favicon").value(hasItem(DEFAULT_FAVICON)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    void getContent() throws Exception {
        // Initialize the database
        insertedContent = contentRepository.saveAndFlush(content);

        // Get the content
        restContentMockMvc
            .perform(get(ENTITY_API_URL_ID, content.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(content.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.uri").value(DEFAULT_URI))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.params").value(DEFAULT_PARAMS))
            .andExpect(jsonPath("$.bodyText").value(DEFAULT_BODY_TEXT.toString()))
            .andExpect(jsonPath("$.textSize").value(DEFAULT_TEXT_SIZE.intValue()))
            .andExpect(jsonPath("$.structuredContent").value(DEFAULT_STRUCTURED_CONTENT.toString()))
            .andExpect(jsonPath("$.structureSize").value(DEFAULT_STRUCTURE_SIZE.intValue()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.subtype").value(DEFAULT_SUBTYPE))
            .andExpect(jsonPath("$.mineType").value(DEFAULT_MINE_TYPE))
            .andExpect(jsonPath("$.publishDate").value(DEFAULT_PUBLISH_DATE.toString()))
            .andExpect(jsonPath("$.offensiveFlag").value(DEFAULT_OFFENSIVE_FLAG))
            .andExpect(jsonPath("$.favicon").value(DEFAULT_FAVICON))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContent() throws Exception {
        // Get the content
        restContentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContent() throws Exception {
        // Initialize the database
        insertedContent = contentRepository.saveAndFlush(content);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the content
        Content updatedContent = contentRepository.findById(content.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContent are not directly saved in db
        em.detach(updatedContent);
        updatedContent
            .title(UPDATED_TITLE)
            .uri(UPDATED_URI)
            .description(UPDATED_DESCRIPTION)
            .path(UPDATED_PATH)
            .source(UPDATED_SOURCE)
            .params(UPDATED_PARAMS)
            .bodyText(UPDATED_BODY_TEXT)
            .textSize(UPDATED_TEXT_SIZE)
            .structuredContent(UPDATED_STRUCTURED_CONTENT)
            .structureSize(UPDATED_STRUCTURE_SIZE)
            .author(UPDATED_AUTHOR)
            .language(UPDATED_LANGUAGE)
            .type(UPDATED_TYPE)
            .subtype(UPDATED_SUBTYPE)
            .mineType(UPDATED_MINE_TYPE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .offensiveFlag(UPDATED_OFFENSIVE_FLAG)
            .favicon(UPDATED_FAVICON)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContent))
            )
            .andExpect(status().isOk());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContentToMatchAllProperties(updatedContent);
    }

    @Test
    @Transactional
    void putNonExistingContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        content.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(put(ENTITY_API_URL_ID, content.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(content)))
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        content.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(content))
            )
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        content.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(content)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContentWithPatch() throws Exception {
        // Initialize the database
        insertedContent = contentRepository.saveAndFlush(content);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the content using partial update
        Content partialUpdatedContent = new Content();
        partialUpdatedContent.setId(content.getId());

        partialUpdatedContent
            .title(UPDATED_TITLE)
            .path(UPDATED_PATH)
            .bodyText(UPDATED_BODY_TEXT)
            .structuredContent(UPDATED_STRUCTURED_CONTENT)
            .language(UPDATED_LANGUAGE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .offensiveFlag(UPDATED_OFFENSIVE_FLAG);

        restContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContent))
            )
            .andExpect(status().isOk());

        // Validate the Content in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContent, content), getPersistedContent(content));
    }

    @Test
    @Transactional
    void fullUpdateContentWithPatch() throws Exception {
        // Initialize the database
        insertedContent = contentRepository.saveAndFlush(content);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the content using partial update
        Content partialUpdatedContent = new Content();
        partialUpdatedContent.setId(content.getId());

        partialUpdatedContent
            .title(UPDATED_TITLE)
            .uri(UPDATED_URI)
            .description(UPDATED_DESCRIPTION)
            .path(UPDATED_PATH)
            .source(UPDATED_SOURCE)
            .params(UPDATED_PARAMS)
            .bodyText(UPDATED_BODY_TEXT)
            .textSize(UPDATED_TEXT_SIZE)
            .structuredContent(UPDATED_STRUCTURED_CONTENT)
            .structureSize(UPDATED_STRUCTURE_SIZE)
            .author(UPDATED_AUTHOR)
            .language(UPDATED_LANGUAGE)
            .type(UPDATED_TYPE)
            .subtype(UPDATED_SUBTYPE)
            .mineType(UPDATED_MINE_TYPE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .offensiveFlag(UPDATED_OFFENSIVE_FLAG)
            .favicon(UPDATED_FAVICON)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContent))
            )
            .andExpect(status().isOk());

        // Validate the Content in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContentUpdatableFieldsEquals(partialUpdatedContent, getPersistedContent(partialUpdatedContent));
    }

    @Test
    @Transactional
    void patchNonExistingContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        content.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, content.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(content))
            )
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        content.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(content))
            )
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        content.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(content)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Content in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContent() throws Exception {
        // Initialize the database
        insertedContent = contentRepository.saveAndFlush(content);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the content
        restContentMockMvc
            .perform(delete(ENTITY_API_URL_ID, content.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contentRepository.count();
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

    protected Content getPersistedContent(Content content) {
        return contentRepository.findById(content.getId()).orElseThrow();
    }

    protected void assertPersistedContentToMatchAllProperties(Content expectedContent) {
        assertContentAllPropertiesEquals(expectedContent, getPersistedContent(expectedContent));
    }

    protected void assertPersistedContentToMatchUpdatableProperties(Content expectedContent) {
        assertContentAllUpdatablePropertiesEquals(expectedContent, getPersistedContent(expectedContent));
    }
}
