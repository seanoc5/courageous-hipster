package com.oconeco.courageous.web.rest;

import static com.oconeco.courageous.domain.SearchConfigurationAsserts.*;
import static com.oconeco.courageous.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconeco.courageous.IntegrationTest;
import com.oconeco.courageous.domain.SearchConfiguration;
import com.oconeco.courageous.repository.SearchConfigurationRepository;
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
 * Integration tests for the {@link SearchConfigurationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SearchConfigurationResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEFAULT_CONFIG = false;
    private static final Boolean UPDATED_DEFAULT_CONFIG = true;

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_PARAMS_JSON = "BBBBBBBBBB";

    private static final String DEFAULT_HEADERS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_HEADERS_JSON = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/search-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SearchConfigurationRepository searchConfigurationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSearchConfigurationMockMvc;

    private SearchConfiguration searchConfiguration;

    private SearchConfiguration insertedSearchConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchConfiguration createEntity() {
        return new SearchConfiguration()
            .label(DEFAULT_LABEL)
            .description(DEFAULT_DESCRIPTION)
            .defaultConfig(DEFAULT_DEFAULT_CONFIG)
            .url(DEFAULT_URL)
            .paramsJson(DEFAULT_PARAMS_JSON)
            .headersJson(DEFAULT_HEADERS_JSON)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchConfiguration createUpdatedEntity() {
        return new SearchConfiguration()
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .defaultConfig(UPDATED_DEFAULT_CONFIG)
            .url(UPDATED_URL)
            .paramsJson(UPDATED_PARAMS_JSON)
            .headersJson(UPDATED_HEADERS_JSON)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);
    }

    @BeforeEach
    public void initTest() {
        searchConfiguration = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSearchConfiguration != null) {
            searchConfigurationRepository.delete(insertedSearchConfiguration);
            insertedSearchConfiguration = null;
        }
    }

    @Test
    @Transactional
    void createSearchConfiguration() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SearchConfiguration
        var returnedSearchConfiguration = om.readValue(
            restSearchConfigurationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchConfiguration)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SearchConfiguration.class
        );

        // Validate the SearchConfiguration in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSearchConfigurationUpdatableFieldsEquals(
            returnedSearchConfiguration,
            getPersistedSearchConfiguration(returnedSearchConfiguration)
        );

        insertedSearchConfiguration = returnedSearchConfiguration;
    }

    @Test
    @Transactional
    void createSearchConfigurationWithExistingId() throws Exception {
        // Create the SearchConfiguration with an existing ID
        searchConfiguration.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchConfiguration)))
            .andExpect(status().isBadRequest());

        // Validate the SearchConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        searchConfiguration.setLabel(null);

        // Create the SearchConfiguration, which fails.

        restSearchConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchConfiguration)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSearchConfigurations() throws Exception {
        // Initialize the database
        insertedSearchConfiguration = searchConfigurationRepository.saveAndFlush(searchConfiguration);

        // Get all the searchConfigurationList
        restSearchConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].defaultConfig").value(hasItem(DEFAULT_DEFAULT_CONFIG.booleanValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].paramsJson").value(hasItem(DEFAULT_PARAMS_JSON.toString())))
            .andExpect(jsonPath("$.[*].headersJson").value(hasItem(DEFAULT_HEADERS_JSON.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getSearchConfiguration() throws Exception {
        // Initialize the database
        insertedSearchConfiguration = searchConfigurationRepository.saveAndFlush(searchConfiguration);

        // Get the searchConfiguration
        restSearchConfigurationMockMvc
            .perform(get(ENTITY_API_URL_ID, searchConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(searchConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.defaultConfig").value(DEFAULT_DEFAULT_CONFIG.booleanValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.paramsJson").value(DEFAULT_PARAMS_JSON.toString()))
            .andExpect(jsonPath("$.headersJson").value(DEFAULT_HEADERS_JSON.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSearchConfiguration() throws Exception {
        // Get the searchConfiguration
        restSearchConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSearchConfiguration() throws Exception {
        // Initialize the database
        insertedSearchConfiguration = searchConfigurationRepository.saveAndFlush(searchConfiguration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchConfiguration
        SearchConfiguration updatedSearchConfiguration = searchConfigurationRepository.findById(searchConfiguration.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSearchConfiguration are not directly saved in db
        em.detach(updatedSearchConfiguration);
        updatedSearchConfiguration
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .defaultConfig(UPDATED_DEFAULT_CONFIG)
            .url(UPDATED_URL)
            .paramsJson(UPDATED_PARAMS_JSON)
            .headersJson(UPDATED_HEADERS_JSON)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSearchConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSearchConfiguration.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSearchConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the SearchConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSearchConfigurationToMatchAllProperties(updatedSearchConfiguration);
    }

    @Test
    @Transactional
    void putNonExistingSearchConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchConfiguration.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchConfiguration.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSearchConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchConfiguration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearchConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchConfiguration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchConfigurationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchConfiguration)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSearchConfigurationWithPatch() throws Exception {
        // Initialize the database
        insertedSearchConfiguration = searchConfigurationRepository.saveAndFlush(searchConfiguration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchConfiguration using partial update
        SearchConfiguration partialUpdatedSearchConfiguration = new SearchConfiguration();
        partialUpdatedSearchConfiguration.setId(searchConfiguration.getId());

        partialUpdatedSearchConfiguration.description(UPDATED_DESCRIPTION).headersJson(UPDATED_HEADERS_JSON);

        restSearchConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the SearchConfiguration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchConfigurationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSearchConfiguration, searchConfiguration),
            getPersistedSearchConfiguration(searchConfiguration)
        );
    }

    @Test
    @Transactional
    void fullUpdateSearchConfigurationWithPatch() throws Exception {
        // Initialize the database
        insertedSearchConfiguration = searchConfigurationRepository.saveAndFlush(searchConfiguration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchConfiguration using partial update
        SearchConfiguration partialUpdatedSearchConfiguration = new SearchConfiguration();
        partialUpdatedSearchConfiguration.setId(searchConfiguration.getId());

        partialUpdatedSearchConfiguration
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .defaultConfig(UPDATED_DEFAULT_CONFIG)
            .url(UPDATED_URL)
            .paramsJson(UPDATED_PARAMS_JSON)
            .headersJson(UPDATED_HEADERS_JSON)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSearchConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the SearchConfiguration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchConfigurationUpdatableFieldsEquals(
            partialUpdatedSearchConfiguration,
            getPersistedSearchConfiguration(partialUpdatedSearchConfiguration)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSearchConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchConfiguration.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, searchConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearchConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchConfiguration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearchConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchConfiguration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchConfigurationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(searchConfiguration)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSearchConfiguration() throws Exception {
        // Initialize the database
        insertedSearchConfiguration = searchConfigurationRepository.saveAndFlush(searchConfiguration);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the searchConfiguration
        restSearchConfigurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, searchConfiguration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return searchConfigurationRepository.count();
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

    protected SearchConfiguration getPersistedSearchConfiguration(SearchConfiguration searchConfiguration) {
        return searchConfigurationRepository.findById(searchConfiguration.getId()).orElseThrow();
    }

    protected void assertPersistedSearchConfigurationToMatchAllProperties(SearchConfiguration expectedSearchConfiguration) {
        assertSearchConfigurationAllPropertiesEquals(
            expectedSearchConfiguration,
            getPersistedSearchConfiguration(expectedSearchConfiguration)
        );
    }

    protected void assertPersistedSearchConfigurationToMatchUpdatableProperties(SearchConfiguration expectedSearchConfiguration) {
        assertSearchConfigurationAllUpdatablePropertiesEquals(
            expectedSearchConfiguration,
            getPersistedSearchConfiguration(expectedSearchConfiguration)
        );
    }
}
