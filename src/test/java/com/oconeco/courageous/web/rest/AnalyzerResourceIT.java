package com.oconeco.courageous.web.rest;

import static com.oconeco.courageous.domain.AnalyzerAsserts.*;
import static com.oconeco.courageous.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconeco.courageous.IntegrationTest;
import com.oconeco.courageous.domain.Analyzer;
import com.oconeco.courageous.repository.AnalyzerRepository;
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
 * Integration tests for the {@link AnalyzerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnalyzerResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/analyzers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AnalyzerRepository analyzerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnalyzerMockMvc;

    private Analyzer analyzer;

    private Analyzer insertedAnalyzer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Analyzer createEntity() {
        return new Analyzer()
            .label(DEFAULT_LABEL)
            .description(DEFAULT_DESCRIPTION)
            .code(DEFAULT_CODE)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Analyzer createUpdatedEntity() {
        return new Analyzer()
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .code(UPDATED_CODE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);
    }

    @BeforeEach
    public void initTest() {
        analyzer = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAnalyzer != null) {
            analyzerRepository.delete(insertedAnalyzer);
            insertedAnalyzer = null;
        }
    }

    @Test
    @Transactional
    void createAnalyzer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Analyzer
        var returnedAnalyzer = om.readValue(
            restAnalyzerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(analyzer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Analyzer.class
        );

        // Validate the Analyzer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAnalyzerUpdatableFieldsEquals(returnedAnalyzer, getPersistedAnalyzer(returnedAnalyzer));

        insertedAnalyzer = returnedAnalyzer;
    }

    @Test
    @Transactional
    void createAnalyzerWithExistingId() throws Exception {
        // Create the Analyzer with an existing ID
        analyzer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalyzerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(analyzer)))
            .andExpect(status().isBadRequest());

        // Validate the Analyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        analyzer.setLabel(null);

        // Create the Analyzer, which fails.

        restAnalyzerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(analyzer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAnalyzers() throws Exception {
        // Initialize the database
        insertedAnalyzer = analyzerRepository.saveAndFlush(analyzer);

        // Get all the analyzerList
        restAnalyzerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analyzer.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getAnalyzer() throws Exception {
        // Initialize the database
        insertedAnalyzer = analyzerRepository.saveAndFlush(analyzer);

        // Get the analyzer
        restAnalyzerMockMvc
            .perform(get(ENTITY_API_URL_ID, analyzer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(analyzer.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAnalyzer() throws Exception {
        // Get the analyzer
        restAnalyzerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAnalyzer() throws Exception {
        // Initialize the database
        insertedAnalyzer = analyzerRepository.saveAndFlush(analyzer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the analyzer
        Analyzer updatedAnalyzer = analyzerRepository.findById(analyzer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAnalyzer are not directly saved in db
        em.detach(updatedAnalyzer);
        updatedAnalyzer
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .code(UPDATED_CODE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restAnalyzerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnalyzer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAnalyzer))
            )
            .andExpect(status().isOk());

        // Validate the Analyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAnalyzerToMatchAllProperties(updatedAnalyzer);
    }

    @Test
    @Transactional
    void putNonExistingAnalyzer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analyzer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalyzerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, analyzer.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(analyzer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Analyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnalyzer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analyzer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyzerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(analyzer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Analyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnalyzer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analyzer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyzerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(analyzer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Analyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnalyzerWithPatch() throws Exception {
        // Initialize the database
        insertedAnalyzer = analyzerRepository.saveAndFlush(analyzer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the analyzer using partial update
        Analyzer partialUpdatedAnalyzer = new Analyzer();
        partialUpdatedAnalyzer.setId(analyzer.getId());

        partialUpdatedAnalyzer.description(UPDATED_DESCRIPTION).lastUpdated(UPDATED_LAST_UPDATED);

        restAnalyzerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnalyzer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnalyzer))
            )
            .andExpect(status().isOk());

        // Validate the Analyzer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnalyzerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAnalyzer, analyzer), getPersistedAnalyzer(analyzer));
    }

    @Test
    @Transactional
    void fullUpdateAnalyzerWithPatch() throws Exception {
        // Initialize the database
        insertedAnalyzer = analyzerRepository.saveAndFlush(analyzer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the analyzer using partial update
        Analyzer partialUpdatedAnalyzer = new Analyzer();
        partialUpdatedAnalyzer.setId(analyzer.getId());

        partialUpdatedAnalyzer
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .code(UPDATED_CODE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restAnalyzerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnalyzer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnalyzer))
            )
            .andExpect(status().isOk());

        // Validate the Analyzer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnalyzerUpdatableFieldsEquals(partialUpdatedAnalyzer, getPersistedAnalyzer(partialUpdatedAnalyzer));
    }

    @Test
    @Transactional
    void patchNonExistingAnalyzer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analyzer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalyzerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, analyzer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(analyzer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Analyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnalyzer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analyzer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyzerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(analyzer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Analyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnalyzer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analyzer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalyzerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(analyzer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Analyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnalyzer() throws Exception {
        // Initialize the database
        insertedAnalyzer = analyzerRepository.saveAndFlush(analyzer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the analyzer
        restAnalyzerMockMvc
            .perform(delete(ENTITY_API_URL_ID, analyzer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return analyzerRepository.count();
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

    protected Analyzer getPersistedAnalyzer(Analyzer analyzer) {
        return analyzerRepository.findById(analyzer.getId()).orElseThrow();
    }

    protected void assertPersistedAnalyzerToMatchAllProperties(Analyzer expectedAnalyzer) {
        assertAnalyzerAllPropertiesEquals(expectedAnalyzer, getPersistedAnalyzer(expectedAnalyzer));
    }

    protected void assertPersistedAnalyzerToMatchUpdatableProperties(Analyzer expectedAnalyzer) {
        assertAnalyzerAllUpdatablePropertiesEquals(expectedAnalyzer, getPersistedAnalyzer(expectedAnalyzer));
    }
}
