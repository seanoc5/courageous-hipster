package com.oconeco.courageous.web.rest;

import static com.oconeco.courageous.domain.ThingTypeAsserts.*;
import static com.oconeco.courageous.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconeco.courageous.IntegrationTest;
import com.oconeco.courageous.domain.ThingType;
import com.oconeco.courageous.repository.ThingTypeRepository;
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
 * Integration tests for the {@link ThingTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ThingTypeResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_CLASS = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_CLASS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/thing-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ThingTypeRepository thingTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restThingTypeMockMvc;

    private ThingType thingType;

    private ThingType insertedThingType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ThingType createEntity() {
        return new ThingType()
            .label(DEFAULT_LABEL)
            .parentClass(DEFAULT_PARENT_CLASS)
            .descrption(DEFAULT_DESCRPTION)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ThingType createUpdatedEntity() {
        return new ThingType()
            .label(UPDATED_LABEL)
            .parentClass(UPDATED_PARENT_CLASS)
            .descrption(UPDATED_DESCRPTION)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);
    }

    @BeforeEach
    public void initTest() {
        thingType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedThingType != null) {
            thingTypeRepository.delete(insertedThingType);
            insertedThingType = null;
        }
    }

    @Test
    @Transactional
    void createThingType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ThingType
        var returnedThingType = om.readValue(
            restThingTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(thingType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ThingType.class
        );

        // Validate the ThingType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertThingTypeUpdatableFieldsEquals(returnedThingType, getPersistedThingType(returnedThingType));

        insertedThingType = returnedThingType;
    }

    @Test
    @Transactional
    void createThingTypeWithExistingId() throws Exception {
        // Create the ThingType with an existing ID
        thingType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restThingTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(thingType)))
            .andExpect(status().isBadRequest());

        // Validate the ThingType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        thingType.setLabel(null);

        // Create the ThingType, which fails.

        restThingTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(thingType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllThingTypes() throws Exception {
        // Initialize the database
        insertedThingType = thingTypeRepository.saveAndFlush(thingType);

        // Get all the thingTypeList
        restThingTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(thingType.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].parentClass").value(hasItem(DEFAULT_PARENT_CLASS)))
            .andExpect(jsonPath("$.[*].descrption").value(hasItem(DEFAULT_DESCRPTION)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getThingType() throws Exception {
        // Initialize the database
        insertedThingType = thingTypeRepository.saveAndFlush(thingType);

        // Get the thingType
        restThingTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, thingType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(thingType.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.parentClass").value(DEFAULT_PARENT_CLASS))
            .andExpect(jsonPath("$.descrption").value(DEFAULT_DESCRPTION))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingThingType() throws Exception {
        // Get the thingType
        restThingTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingThingType() throws Exception {
        // Initialize the database
        insertedThingType = thingTypeRepository.saveAndFlush(thingType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the thingType
        ThingType updatedThingType = thingTypeRepository.findById(thingType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedThingType are not directly saved in db
        em.detach(updatedThingType);
        updatedThingType
            .label(UPDATED_LABEL)
            .parentClass(UPDATED_PARENT_CLASS)
            .descrption(UPDATED_DESCRPTION)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restThingTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedThingType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedThingType))
            )
            .andExpect(status().isOk());

        // Validate the ThingType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedThingTypeToMatchAllProperties(updatedThingType);
    }

    @Test
    @Transactional
    void putNonExistingThingType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        thingType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThingTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, thingType.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(thingType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThingType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchThingType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        thingType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThingTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(thingType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThingType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamThingType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        thingType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThingTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(thingType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ThingType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateThingTypeWithPatch() throws Exception {
        // Initialize the database
        insertedThingType = thingTypeRepository.saveAndFlush(thingType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the thingType using partial update
        ThingType partialUpdatedThingType = new ThingType();
        partialUpdatedThingType.setId(thingType.getId());

        partialUpdatedThingType.label(UPDATED_LABEL).dateCreated(UPDATED_DATE_CREATED).lastUpdated(UPDATED_LAST_UPDATED);

        restThingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThingType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedThingType))
            )
            .andExpect(status().isOk());

        // Validate the ThingType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertThingTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedThingType, thingType),
            getPersistedThingType(thingType)
        );
    }

    @Test
    @Transactional
    void fullUpdateThingTypeWithPatch() throws Exception {
        // Initialize the database
        insertedThingType = thingTypeRepository.saveAndFlush(thingType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the thingType using partial update
        ThingType partialUpdatedThingType = new ThingType();
        partialUpdatedThingType.setId(thingType.getId());

        partialUpdatedThingType
            .label(UPDATED_LABEL)
            .parentClass(UPDATED_PARENT_CLASS)
            .descrption(UPDATED_DESCRPTION)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restThingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThingType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedThingType))
            )
            .andExpect(status().isOk());

        // Validate the ThingType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertThingTypeUpdatableFieldsEquals(partialUpdatedThingType, getPersistedThingType(partialUpdatedThingType));
    }

    @Test
    @Transactional
    void patchNonExistingThingType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        thingType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, thingType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(thingType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThingType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchThingType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        thingType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(thingType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThingType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamThingType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        thingType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThingTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(thingType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ThingType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteThingType() throws Exception {
        // Initialize the database
        insertedThingType = thingTypeRepository.saveAndFlush(thingType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the thingType
        restThingTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, thingType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return thingTypeRepository.count();
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

    protected ThingType getPersistedThingType(ThingType thingType) {
        return thingTypeRepository.findById(thingType.getId()).orElseThrow();
    }

    protected void assertPersistedThingTypeToMatchAllProperties(ThingType expectedThingType) {
        assertThingTypeAllPropertiesEquals(expectedThingType, getPersistedThingType(expectedThingType));
    }

    protected void assertPersistedThingTypeToMatchUpdatableProperties(ThingType expectedThingType) {
        assertThingTypeAllUpdatablePropertiesEquals(expectedThingType, getPersistedThingType(expectedThingType));
    }
}
