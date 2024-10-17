package com.oconeco.courageous.web.rest;

import static com.oconeco.courageous.domain.ContextAsserts.*;
import static com.oconeco.courageous.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconeco.courageous.IntegrationTest;
import com.oconeco.courageous.domain.Context;
import com.oconeco.courageous.repository.ContextRepository;
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
 * Integration tests for the {@link ContextResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContextResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_TIME = "AAAAAAAAAA";
    private static final String UPDATED_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_INTENT = "AAAAAAAAAA";
    private static final String UPDATED_INTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEFAULT_CONTEXT = false;
    private static final Boolean UPDATED_DEFAULT_CONTEXT = true;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/contexts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContextRepository contextRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContextMockMvc;

    private Context context;

    private Context insertedContext;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Context createEntity() {
        return new Context()
            .label(DEFAULT_LABEL)
            .description(DEFAULT_DESCRIPTION)
            .level(DEFAULT_LEVEL)
            .time(DEFAULT_TIME)
            .location(DEFAULT_LOCATION)
            .intent(DEFAULT_INTENT)
            .defaultContext(DEFAULT_DEFAULT_CONTEXT)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastUpdate(DEFAULT_LAST_UPDATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Context createUpdatedEntity() {
        return new Context()
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .level(UPDATED_LEVEL)
            .time(UPDATED_TIME)
            .location(UPDATED_LOCATION)
            .intent(UPDATED_INTENT)
            .defaultContext(UPDATED_DEFAULT_CONTEXT)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdate(UPDATED_LAST_UPDATE);
    }

    @BeforeEach
    public void initTest() {
        context = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedContext != null) {
            contextRepository.delete(insertedContext);
            insertedContext = null;
        }
    }

    @Test
    @Transactional
    void createContext() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Context
        var returnedContext = om.readValue(
            restContextMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(context)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Context.class
        );

        // Validate the Context in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContextUpdatableFieldsEquals(returnedContext, getPersistedContext(returnedContext));

        insertedContext = returnedContext;
    }

    @Test
    @Transactional
    void createContextWithExistingId() throws Exception {
        // Create the Context with an existing ID
        context.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(context)))
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        context.setLabel(null);

        // Create the Context, which fails.

        restContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(context)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContexts() throws Exception {
        // Initialize the database
        insertedContext = contextRepository.saveAndFlush(context);

        // Get all the contextList
        restContextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(context.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].intent").value(hasItem(DEFAULT_INTENT)))
            .andExpect(jsonPath("$.[*].defaultContext").value(hasItem(DEFAULT_DEFAULT_CONTEXT.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())));
    }

    @Test
    @Transactional
    void getContext() throws Exception {
        // Initialize the database
        insertedContext = contextRepository.saveAndFlush(context);

        // Get the context
        restContextMockMvc
            .perform(get(ENTITY_API_URL_ID, context.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(context.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.intent").value(DEFAULT_INTENT))
            .andExpect(jsonPath("$.defaultContext").value(DEFAULT_DEFAULT_CONTEXT.booleanValue()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContext() throws Exception {
        // Get the context
        restContextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContext() throws Exception {
        // Initialize the database
        insertedContext = contextRepository.saveAndFlush(context);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the context
        Context updatedContext = contextRepository.findById(context.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContext are not directly saved in db
        em.detach(updatedContext);
        updatedContext
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .level(UPDATED_LEVEL)
            .time(UPDATED_TIME)
            .location(UPDATED_LOCATION)
            .intent(UPDATED_INTENT)
            .defaultContext(UPDATED_DEFAULT_CONTEXT)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContext.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContext))
            )
            .andExpect(status().isOk());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContextToMatchAllProperties(updatedContext);
    }

    @Test
    @Transactional
    void putNonExistingContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        context.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(put(ENTITY_API_URL_ID, context.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(context)))
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        context.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(context))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        context.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(context)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContextWithPatch() throws Exception {
        // Initialize the database
        insertedContext = contextRepository.saveAndFlush(context);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the context using partial update
        Context partialUpdatedContext = new Context();
        partialUpdatedContext.setId(context.getId());

        partialUpdatedContext.time(UPDATED_TIME);

        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContext.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContext))
            )
            .andExpect(status().isOk());

        // Validate the Context in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContextUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContext, context), getPersistedContext(context));
    }

    @Test
    @Transactional
    void fullUpdateContextWithPatch() throws Exception {
        // Initialize the database
        insertedContext = contextRepository.saveAndFlush(context);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the context using partial update
        Context partialUpdatedContext = new Context();
        partialUpdatedContext.setId(context.getId());

        partialUpdatedContext
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .level(UPDATED_LEVEL)
            .time(UPDATED_TIME)
            .location(UPDATED_LOCATION)
            .intent(UPDATED_INTENT)
            .defaultContext(UPDATED_DEFAULT_CONTEXT)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContext.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContext))
            )
            .andExpect(status().isOk());

        // Validate the Context in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContextUpdatableFieldsEquals(partialUpdatedContext, getPersistedContext(partialUpdatedContext));
    }

    @Test
    @Transactional
    void patchNonExistingContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        context.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, context.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(context))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        context.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(context))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        context.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(context)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContext() throws Exception {
        // Initialize the database
        insertedContext = contextRepository.saveAndFlush(context);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the context
        restContextMockMvc
            .perform(delete(ENTITY_API_URL_ID, context.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contextRepository.count();
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

    protected Context getPersistedContext(Context context) {
        return contextRepository.findById(context.getId()).orElseThrow();
    }

    protected void assertPersistedContextToMatchAllProperties(Context expectedContext) {
        assertContextAllPropertiesEquals(expectedContext, getPersistedContext(expectedContext));
    }

    protected void assertPersistedContextToMatchUpdatableProperties(Context expectedContext) {
        assertContextAllUpdatablePropertiesEquals(expectedContext, getPersistedContext(expectedContext));
    }
}
