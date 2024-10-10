package com.oconeco.courageous.web.rest;

import static com.oconeco.courageous.domain.SearchResultAsserts.*;
import static com.oconeco.courageous.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconeco.courageous.IntegrationTest;
import com.oconeco.courageous.domain.SearchResult;
import com.oconeco.courageous.repository.SearchResultRepository;
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
 * Integration tests for the {@link SearchResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SearchResultResourceIT {

    private static final String DEFAULT_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_QUERY = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSE_BODY = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_BODY = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS_CODE = 1;
    private static final Integer UPDATED_STATUS_CODE = 2;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/search-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SearchResultRepository searchResultRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSearchResultMockMvc;

    private SearchResult searchResult;

    private SearchResult insertedSearchResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchResult createEntity() {
        return new SearchResult()
            .query(DEFAULT_QUERY)
            .type(DEFAULT_TYPE)
            .responseBody(DEFAULT_RESPONSE_BODY)
            .statusCode(DEFAULT_STATUS_CODE)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchResult createUpdatedEntity() {
        return new SearchResult()
            .query(UPDATED_QUERY)
            .type(UPDATED_TYPE)
            .responseBody(UPDATED_RESPONSE_BODY)
            .statusCode(UPDATED_STATUS_CODE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);
    }

    @BeforeEach
    public void initTest() {
        searchResult = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSearchResult != null) {
            searchResultRepository.delete(insertedSearchResult);
            insertedSearchResult = null;
        }
    }

    @Test
    @Transactional
    void createSearchResult() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SearchResult
        var returnedSearchResult = om.readValue(
            restSearchResultMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchResult)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SearchResult.class
        );

        // Validate the SearchResult in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSearchResultUpdatableFieldsEquals(returnedSearchResult, getPersistedSearchResult(returnedSearchResult));

        insertedSearchResult = returnedSearchResult;
    }

    @Test
    @Transactional
    void createSearchResultWithExistingId() throws Exception {
        // Create the SearchResult with an existing ID
        searchResult.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchResult)))
            .andExpect(status().isBadRequest());

        // Validate the SearchResult in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQueryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        searchResult.setQuery(null);

        // Create the SearchResult, which fails.

        restSearchResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchResult)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSearchResults() throws Exception {
        // Initialize the database
        insertedSearchResult = searchResultRepository.saveAndFlush(searchResult);

        // Get all the searchResultList
        restSearchResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].responseBody").value(hasItem(DEFAULT_RESPONSE_BODY.toString())))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getSearchResult() throws Exception {
        // Initialize the database
        insertedSearchResult = searchResultRepository.saveAndFlush(searchResult);

        // Get the searchResult
        restSearchResultMockMvc
            .perform(get(ENTITY_API_URL_ID, searchResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(searchResult.getId().intValue()))
            .andExpect(jsonPath("$.query").value(DEFAULT_QUERY))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.responseBody").value(DEFAULT_RESPONSE_BODY.toString()))
            .andExpect(jsonPath("$.statusCode").value(DEFAULT_STATUS_CODE))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSearchResult() throws Exception {
        // Get the searchResult
        restSearchResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSearchResult() throws Exception {
        // Initialize the database
        insertedSearchResult = searchResultRepository.saveAndFlush(searchResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchResult
        SearchResult updatedSearchResult = searchResultRepository.findById(searchResult.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSearchResult are not directly saved in db
        em.detach(updatedSearchResult);
        updatedSearchResult
            .query(UPDATED_QUERY)
            .type(UPDATED_TYPE)
            .responseBody(UPDATED_RESPONSE_BODY)
            .statusCode(UPDATED_STATUS_CODE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSearchResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSearchResult.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSearchResult))
            )
            .andExpect(status().isOk());

        // Validate the SearchResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSearchResultToMatchAllProperties(updatedSearchResult);
    }

    @Test
    @Transactional
    void putNonExistingSearchResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchResult.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchResult.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSearchResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchResult.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearchResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchResult.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchResult)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSearchResultWithPatch() throws Exception {
        // Initialize the database
        insertedSearchResult = searchResultRepository.saveAndFlush(searchResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchResult using partial update
        SearchResult partialUpdatedSearchResult = new SearchResult();
        partialUpdatedSearchResult.setId(searchResult.getId());

        partialUpdatedSearchResult.query(UPDATED_QUERY).dateCreated(UPDATED_DATE_CREATED).lastUpdated(UPDATED_LAST_UPDATED);

        restSearchResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchResult))
            )
            .andExpect(status().isOk());

        // Validate the SearchResult in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchResultUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSearchResult, searchResult),
            getPersistedSearchResult(searchResult)
        );
    }

    @Test
    @Transactional
    void fullUpdateSearchResultWithPatch() throws Exception {
        // Initialize the database
        insertedSearchResult = searchResultRepository.saveAndFlush(searchResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchResult using partial update
        SearchResult partialUpdatedSearchResult = new SearchResult();
        partialUpdatedSearchResult.setId(searchResult.getId());

        partialUpdatedSearchResult
            .query(UPDATED_QUERY)
            .type(UPDATED_TYPE)
            .responseBody(UPDATED_RESPONSE_BODY)
            .statusCode(UPDATED_STATUS_CODE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSearchResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchResult))
            )
            .andExpect(status().isOk());

        // Validate the SearchResult in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchResultUpdatableFieldsEquals(partialUpdatedSearchResult, getPersistedSearchResult(partialUpdatedSearchResult));
    }

    @Test
    @Transactional
    void patchNonExistingSearchResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchResult.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, searchResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearchResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchResult.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearchResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchResult.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchResultMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(searchResult)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSearchResult() throws Exception {
        // Initialize the database
        insertedSearchResult = searchResultRepository.saveAndFlush(searchResult);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the searchResult
        restSearchResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, searchResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return searchResultRepository.count();
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

    protected SearchResult getPersistedSearchResult(SearchResult searchResult) {
        return searchResultRepository.findById(searchResult.getId()).orElseThrow();
    }

    protected void assertPersistedSearchResultToMatchAllProperties(SearchResult expectedSearchResult) {
        assertSearchResultAllPropertiesEquals(expectedSearchResult, getPersistedSearchResult(expectedSearchResult));
    }

    protected void assertPersistedSearchResultToMatchUpdatableProperties(SearchResult expectedSearchResult) {
        assertSearchResultAllUpdatablePropertiesEquals(expectedSearchResult, getPersistedSearchResult(expectedSearchResult));
    }
}
