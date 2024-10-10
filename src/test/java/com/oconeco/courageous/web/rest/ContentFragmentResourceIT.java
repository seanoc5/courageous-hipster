package com.oconeco.courageous.web.rest;

import static com.oconeco.courageous.domain.ContentFragmentAsserts.*;
import static com.oconeco.courageous.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconeco.courageous.IntegrationTest;
import com.oconeco.courageous.domain.ContentFragment;
import com.oconeco.courageous.repository.ContentFragmentRepository;
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
 * Integration tests for the {@link ContentFragmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContentFragmentResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STRUCTURED_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_STRUCTURED_CONTENT = "BBBBBBBBBB";

    private static final Long DEFAULT_START_POS = 1L;
    private static final Long UPDATED_START_POS = 2L;

    private static final Long DEFAULT_END_POS = 1L;
    private static final Long UPDATED_END_POS = 2L;

    private static final Long DEFAULT_START_TERM_NUM = 1L;
    private static final Long UPDATED_START_TERM_NUM = 2L;

    private static final Long DEFAULT_END_TERM_NUM = 1L;
    private static final Long UPDATED_END_TERM_NUM = 2L;

    private static final String DEFAULT_SUBTYPE = "AAAAAAAAAA";
    private static final String UPDATED_SUBTYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/content-fragments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContentFragmentRepository contentFragmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentFragmentMockMvc;

    private ContentFragment contentFragment;

    private ContentFragment insertedContentFragment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentFragment createEntity() {
        return new ContentFragment()
            .label(DEFAULT_LABEL)
            .text(DEFAULT_TEXT)
            .description(DEFAULT_DESCRIPTION)
            .structuredContent(DEFAULT_STRUCTURED_CONTENT)
            .startPos(DEFAULT_START_POS)
            .endPos(DEFAULT_END_POS)
            .startTermNum(DEFAULT_START_TERM_NUM)
            .endTermNum(DEFAULT_END_TERM_NUM)
            .subtype(DEFAULT_SUBTYPE)
            .language(DEFAULT_LANGUAGE)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentFragment createUpdatedEntity() {
        return new ContentFragment()
            .label(UPDATED_LABEL)
            .text(UPDATED_TEXT)
            .description(UPDATED_DESCRIPTION)
            .structuredContent(UPDATED_STRUCTURED_CONTENT)
            .startPos(UPDATED_START_POS)
            .endPos(UPDATED_END_POS)
            .startTermNum(UPDATED_START_TERM_NUM)
            .endTermNum(UPDATED_END_TERM_NUM)
            .subtype(UPDATED_SUBTYPE)
            .language(UPDATED_LANGUAGE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);
    }

    @BeforeEach
    public void initTest() {
        contentFragment = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedContentFragment != null) {
            contentFragmentRepository.delete(insertedContentFragment);
            insertedContentFragment = null;
        }
    }

    @Test
    @Transactional
    void createContentFragment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContentFragment
        var returnedContentFragment = om.readValue(
            restContentFragmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contentFragment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContentFragment.class
        );

        // Validate the ContentFragment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContentFragmentUpdatableFieldsEquals(returnedContentFragment, getPersistedContentFragment(returnedContentFragment));

        insertedContentFragment = returnedContentFragment;
    }

    @Test
    @Transactional
    void createContentFragmentWithExistingId() throws Exception {
        // Create the ContentFragment with an existing ID
        contentFragment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentFragmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contentFragment)))
            .andExpect(status().isBadRequest());

        // Validate the ContentFragment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contentFragment.setLabel(null);

        // Create the ContentFragment, which fails.

        restContentFragmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contentFragment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContentFragments() throws Exception {
        // Initialize the database
        insertedContentFragment = contentFragmentRepository.saveAndFlush(contentFragment);

        // Get all the contentFragmentList
        restContentFragmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contentFragment.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].structuredContent").value(hasItem(DEFAULT_STRUCTURED_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].startPos").value(hasItem(DEFAULT_START_POS.intValue())))
            .andExpect(jsonPath("$.[*].endPos").value(hasItem(DEFAULT_END_POS.intValue())))
            .andExpect(jsonPath("$.[*].startTermNum").value(hasItem(DEFAULT_START_TERM_NUM.intValue())))
            .andExpect(jsonPath("$.[*].endTermNum").value(hasItem(DEFAULT_END_TERM_NUM.intValue())))
            .andExpect(jsonPath("$.[*].subtype").value(hasItem(DEFAULT_SUBTYPE)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getContentFragment() throws Exception {
        // Initialize the database
        insertedContentFragment = contentFragmentRepository.saveAndFlush(contentFragment);

        // Get the contentFragment
        restContentFragmentMockMvc
            .perform(get(ENTITY_API_URL_ID, contentFragment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contentFragment.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.structuredContent").value(DEFAULT_STRUCTURED_CONTENT.toString()))
            .andExpect(jsonPath("$.startPos").value(DEFAULT_START_POS.intValue()))
            .andExpect(jsonPath("$.endPos").value(DEFAULT_END_POS.intValue()))
            .andExpect(jsonPath("$.startTermNum").value(DEFAULT_START_TERM_NUM.intValue()))
            .andExpect(jsonPath("$.endTermNum").value(DEFAULT_END_TERM_NUM.intValue()))
            .andExpect(jsonPath("$.subtype").value(DEFAULT_SUBTYPE))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContentFragment() throws Exception {
        // Get the contentFragment
        restContentFragmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContentFragment() throws Exception {
        // Initialize the database
        insertedContentFragment = contentFragmentRepository.saveAndFlush(contentFragment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contentFragment
        ContentFragment updatedContentFragment = contentFragmentRepository.findById(contentFragment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContentFragment are not directly saved in db
        em.detach(updatedContentFragment);
        updatedContentFragment
            .label(UPDATED_LABEL)
            .text(UPDATED_TEXT)
            .description(UPDATED_DESCRIPTION)
            .structuredContent(UPDATED_STRUCTURED_CONTENT)
            .startPos(UPDATED_START_POS)
            .endPos(UPDATED_END_POS)
            .startTermNum(UPDATED_START_TERM_NUM)
            .endTermNum(UPDATED_END_TERM_NUM)
            .subtype(UPDATED_SUBTYPE)
            .language(UPDATED_LANGUAGE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restContentFragmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContentFragment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContentFragment))
            )
            .andExpect(status().isOk());

        // Validate the ContentFragment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContentFragmentToMatchAllProperties(updatedContentFragment);
    }

    @Test
    @Transactional
    void putNonExistingContentFragment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contentFragment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentFragmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentFragment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contentFragment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentFragment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContentFragment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contentFragment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentFragmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contentFragment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentFragment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContentFragment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contentFragment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentFragmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contentFragment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentFragment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContentFragmentWithPatch() throws Exception {
        // Initialize the database
        insertedContentFragment = contentFragmentRepository.saveAndFlush(contentFragment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contentFragment using partial update
        ContentFragment partialUpdatedContentFragment = new ContentFragment();
        partialUpdatedContentFragment.setId(contentFragment.getId());

        partialUpdatedContentFragment
            .text(UPDATED_TEXT)
            .description(UPDATED_DESCRIPTION)
            .structuredContent(UPDATED_STRUCTURED_CONTENT)
            .startPos(UPDATED_START_POS)
            .endPos(UPDATED_END_POS)
            .startTermNum(UPDATED_START_TERM_NUM)
            .endTermNum(UPDATED_END_TERM_NUM);

        restContentFragmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentFragment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContentFragment))
            )
            .andExpect(status().isOk());

        // Validate the ContentFragment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContentFragmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContentFragment, contentFragment),
            getPersistedContentFragment(contentFragment)
        );
    }

    @Test
    @Transactional
    void fullUpdateContentFragmentWithPatch() throws Exception {
        // Initialize the database
        insertedContentFragment = contentFragmentRepository.saveAndFlush(contentFragment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contentFragment using partial update
        ContentFragment partialUpdatedContentFragment = new ContentFragment();
        partialUpdatedContentFragment.setId(contentFragment.getId());

        partialUpdatedContentFragment
            .label(UPDATED_LABEL)
            .text(UPDATED_TEXT)
            .description(UPDATED_DESCRIPTION)
            .structuredContent(UPDATED_STRUCTURED_CONTENT)
            .startPos(UPDATED_START_POS)
            .endPos(UPDATED_END_POS)
            .startTermNum(UPDATED_START_TERM_NUM)
            .endTermNum(UPDATED_END_TERM_NUM)
            .subtype(UPDATED_SUBTYPE)
            .language(UPDATED_LANGUAGE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restContentFragmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentFragment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContentFragment))
            )
            .andExpect(status().isOk());

        // Validate the ContentFragment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContentFragmentUpdatableFieldsEquals(
            partialUpdatedContentFragment,
            getPersistedContentFragment(partialUpdatedContentFragment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingContentFragment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contentFragment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentFragmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contentFragment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contentFragment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentFragment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContentFragment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contentFragment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentFragmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contentFragment))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentFragment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContentFragment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contentFragment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentFragmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contentFragment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentFragment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContentFragment() throws Exception {
        // Initialize the database
        insertedContentFragment = contentFragmentRepository.saveAndFlush(contentFragment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contentFragment
        restContentFragmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, contentFragment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contentFragmentRepository.count();
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

    protected ContentFragment getPersistedContentFragment(ContentFragment contentFragment) {
        return contentFragmentRepository.findById(contentFragment.getId()).orElseThrow();
    }

    protected void assertPersistedContentFragmentToMatchAllProperties(ContentFragment expectedContentFragment) {
        assertContentFragmentAllPropertiesEquals(expectedContentFragment, getPersistedContentFragment(expectedContentFragment));
    }

    protected void assertPersistedContentFragmentToMatchUpdatableProperties(ContentFragment expectedContentFragment) {
        assertContentFragmentAllUpdatablePropertiesEquals(expectedContentFragment, getPersistedContentFragment(expectedContentFragment));
    }
}
