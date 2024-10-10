package com.oconeco.courageous.web.rest;

import static com.oconeco.courageous.domain.SearchAsserts.*;
import static com.oconeco.courageous.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconeco.courageous.IntegrationTest;
import com.oconeco.courageous.domain.Search;
import com.oconeco.courageous.repository.SearchRepository;
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
 * Integration tests for the {@link SearchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SearchResourceIT {

    private static final String DEFAULT_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_QUERY = "BBBBBBBBBB";

    private static final String DEFAULT_ADDITIONAL_PARAMS = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_PARAMS = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/searches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSearchMockMvc;

    private Search search;

    private Search insertedSearch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Search createEntity() {
        return new Search()
            .query(DEFAULT_QUERY)
            .additionalParams(DEFAULT_ADDITIONAL_PARAMS)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Search createUpdatedEntity() {
        return new Search()
            .query(UPDATED_QUERY)
            .additionalParams(UPDATED_ADDITIONAL_PARAMS)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);
    }

    @BeforeEach
    public void initTest() {
        search = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSearch != null) {
            searchRepository.delete(insertedSearch);
            insertedSearch = null;
        }
    }

    @Test
    @Transactional
    void createSearch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Search
        var returnedSearch = om.readValue(
            restSearchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(search)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Search.class
        );

        // Validate the Search in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSearchUpdatableFieldsEquals(returnedSearch, getPersistedSearch(returnedSearch));

        insertedSearch = returnedSearch;
    }

    @Test
    @Transactional
    void createSearchWithExistingId() throws Exception {
        // Create the Search with an existing ID
        search.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(search)))
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQueryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        search.setQuery(null);

        // Create the Search, which fails.

        restSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(search)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSearches() throws Exception {
        // Initialize the database
        insertedSearch = searchRepository.saveAndFlush(search);

        // Get all the searchList
        restSearchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(search.getId().intValue())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY)))
            .andExpect(jsonPath("$.[*].additionalParams").value(hasItem(DEFAULT_ADDITIONAL_PARAMS.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getSearch() throws Exception {
        // Initialize the database
        insertedSearch = searchRepository.saveAndFlush(search);

        // Get the search
        restSearchMockMvc
            .perform(get(ENTITY_API_URL_ID, search.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(search.getId().intValue()))
            .andExpect(jsonPath("$.query").value(DEFAULT_QUERY))
            .andExpect(jsonPath("$.additionalParams").value(DEFAULT_ADDITIONAL_PARAMS.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSearch() throws Exception {
        // Get the search
        restSearchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSearch() throws Exception {
        // Initialize the database
        insertedSearch = searchRepository.saveAndFlush(search);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the search
        Search updatedSearch = searchRepository.findById(search.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSearch are not directly saved in db
        em.detach(updatedSearch);
        updatedSearch
            .query(UPDATED_QUERY)
            .additionalParams(UPDATED_ADDITIONAL_PARAMS)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSearch.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSearch))
            )
            .andExpect(status().isOk());

        // Validate the Search in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSearchToMatchAllProperties(updatedSearch);
    }

    @Test
    @Transactional
    void putNonExistingSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        search.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(put(ENTITY_API_URL_ID, search.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(search)))
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        search.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(search))
            )
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        search.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(search)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Search in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSearchWithPatch() throws Exception {
        // Initialize the database
        insertedSearch = searchRepository.saveAndFlush(search);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the search using partial update
        Search partialUpdatedSearch = new Search();
        partialUpdatedSearch.setId(search.getId());

        partialUpdatedSearch.lastUpdated(UPDATED_LAST_UPDATED);

        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearch))
            )
            .andExpect(status().isOk());

        // Validate the Search in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSearch, search), getPersistedSearch(search));
    }

    @Test
    @Transactional
    void fullUpdateSearchWithPatch() throws Exception {
        // Initialize the database
        insertedSearch = searchRepository.saveAndFlush(search);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the search using partial update
        Search partialUpdatedSearch = new Search();
        partialUpdatedSearch.setId(search.getId());

        partialUpdatedSearch
            .query(UPDATED_QUERY)
            .additionalParams(UPDATED_ADDITIONAL_PARAMS)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearch))
            )
            .andExpect(status().isOk());

        // Validate the Search in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchUpdatableFieldsEquals(partialUpdatedSearch, getPersistedSearch(partialUpdatedSearch));
    }

    @Test
    @Transactional
    void patchNonExistingSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        search.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, search.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(search))
            )
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        search.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(search))
            )
            .andExpect(status().isBadRequest());

        // Validate the Search in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        search.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(search)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Search in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSearch() throws Exception {
        // Initialize the database
        insertedSearch = searchRepository.saveAndFlush(search);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the search
        restSearchMockMvc
            .perform(delete(ENTITY_API_URL_ID, search.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return searchRepository.count();
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

    protected Search getPersistedSearch(Search search) {
        return searchRepository.findById(search.getId()).orElseThrow();
    }

    protected void assertPersistedSearchToMatchAllProperties(Search expectedSearch) {
        assertSearchAllPropertiesEquals(expectedSearch, getPersistedSearch(expectedSearch));
    }

    protected void assertPersistedSearchToMatchUpdatableProperties(Search expectedSearch) {
        assertSearchAllUpdatablePropertiesEquals(expectedSearch, getPersistedSearch(expectedSearch));
    }
}
