package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MedicationAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Medication;
import com.mycompany.myapp.domain.MedicationCategory;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.MedicationRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.MedicationService;
import com.mycompany.myapp.service.dto.MedicationDTO;
import com.mycompany.myapp.service.mapper.MedicationMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MedicationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MedicationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATING = 0;
    private static final Integer UPDATED_RATING = 1;
    private static final Integer SMALLER_RATING = 0 - 1;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private MedicationRepository medicationRepositoryMock;

    @Autowired
    private MedicationMapper medicationMapper;

    @Mock
    private MedicationService medicationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicationMockMvc;

    private Medication medication;

    private Medication insertedMedication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medication createEntity(EntityManager em) {
        Medication medication = new Medication().name(DEFAULT_NAME).rating(DEFAULT_RATING).notes(DEFAULT_NOTES);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        medication.setOwner(user);
        return medication;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medication createUpdatedEntity(EntityManager em) {
        Medication updatedMedication = new Medication().name(UPDATED_NAME).rating(UPDATED_RATING).notes(UPDATED_NOTES);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedMedication.setOwner(user);
        return updatedMedication;
    }

    @BeforeEach
    void initTest() {
        medication = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMedication != null) {
            medicationRepository.delete(insertedMedication);
            insertedMedication = null;
        }
    }

    @Test
    @Transactional
    void createMedication() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);
        var returnedMedicationDTO = om.readValue(
            restMedicationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicationDTO.class
        );

        // Validate the Medication in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedication = medicationMapper.toEntity(returnedMedicationDTO);
        assertMedicationUpdatableFieldsEquals(returnedMedication, getPersistedMedication(returnedMedication));

        insertedMedication = returnedMedication;
    }

    @Test
    @Transactional
    void createMedicationWithExistingId() throws Exception {
        // Create the Medication with an existing ID
        medication.setId(1L);
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medication.setName(null);

        // Create the Medication, which fails.
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        restMedicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedications() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList
        restMedicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medication.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(medicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(medicationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(medicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(medicationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMedication() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get the medication
        restMedicationMockMvc
            .perform(get(ENTITY_API_URL_ID, medication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medication.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getMedicationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        Long id = medication.getId();

        defaultMedicationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMedicationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMedicationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMedicationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where name equals to
        defaultMedicationFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMedicationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where name in
        defaultMedicationFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMedicationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where name is not null
        defaultMedicationFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicationsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where name contains
        defaultMedicationFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMedicationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where name does not contain
        defaultMedicationFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMedicationsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where rating equals to
        defaultMedicationFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllMedicationsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where rating in
        defaultMedicationFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllMedicationsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where rating is not null
        defaultMedicationFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicationsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where rating is greater than or equal to
        defaultMedicationFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllMedicationsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where rating is less than or equal to
        defaultMedicationFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllMedicationsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where rating is less than
        defaultMedicationFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllMedicationsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where rating is greater than
        defaultMedicationFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllMedicationsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where notes equals to
        defaultMedicationFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllMedicationsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where notes in
        defaultMedicationFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllMedicationsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where notes is not null
        defaultMedicationFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicationsByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where notes contains
        defaultMedicationFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllMedicationsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList where notes does not contain
        defaultMedicationFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllMedicationsByOwnerIsEqualToSomething() throws Exception {
        User owner;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            medicationRepository.saveAndFlush(medication);
            owner = UserResourceIT.createEntity();
        } else {
            owner = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(owner);
        em.flush();
        medication.setOwner(owner);
        medicationRepository.saveAndFlush(medication);
        Long ownerId = owner.getId();
        // Get all the medicationList where owner equals to ownerId
        defaultMedicationShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the medicationList where owner equals to (ownerId + 1)
        defaultMedicationShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    @Test
    @Transactional
    void getAllMedicationsByCategoriesIsEqualToSomething() throws Exception {
        MedicationCategory categories;
        if (TestUtil.findAll(em, MedicationCategory.class).isEmpty()) {
            medicationRepository.saveAndFlush(medication);
            categories = MedicationCategoryResourceIT.createEntity(em);
        } else {
            categories = TestUtil.findAll(em, MedicationCategory.class).get(0);
        }
        em.persist(categories);
        em.flush();
        medication.addCategories(categories);
        medicationRepository.saveAndFlush(medication);
        Long categoriesId = categories.getId();
        // Get all the medicationList where categories equals to categoriesId
        defaultMedicationShouldBeFound("categoriesId.equals=" + categoriesId);

        // Get all the medicationList where categories equals to (categoriesId + 1)
        defaultMedicationShouldNotBeFound("categoriesId.equals=" + (categoriesId + 1));
    }

    private void defaultMedicationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMedicationShouldBeFound(shouldBeFound);
        defaultMedicationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicationShouldBeFound(String filter) throws Exception {
        restMedicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medication.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restMedicationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedicationShouldNotBeFound(String filter) throws Exception {
        restMedicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedication() throws Exception {
        // Get the medication
        restMedicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedication() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medication
        Medication updatedMedication = medicationRepository.findById(medication.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedication are not directly saved in db
        em.detach(updatedMedication);
        updatedMedication.name(UPDATED_NAME).rating(UPDATED_RATING).notes(UPDATED_NOTES);
        MedicationDTO medicationDTO = medicationMapper.toDto(updatedMedication);

        restMedicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicationToMatchAllProperties(updatedMedication);
    }

    @Test
    @Transactional
    void putNonExistingMedication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medication.setId(longCount.incrementAndGet());

        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medication.setId(longCount.incrementAndGet());

        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medication.setId(longCount.incrementAndGet());

        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicationWithPatch() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medication using partial update
        Medication partialUpdatedMedication = new Medication();
        partialUpdatedMedication.setId(medication.getId());

        partialUpdatedMedication.name(UPDATED_NAME).notes(UPDATED_NOTES);

        restMedicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedication))
            )
            .andExpect(status().isOk());

        // Validate the Medication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedication, medication),
            getPersistedMedication(medication)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicationWithPatch() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medication using partial update
        Medication partialUpdatedMedication = new Medication();
        partialUpdatedMedication.setId(medication.getId());

        partialUpdatedMedication.name(UPDATED_NAME).rating(UPDATED_RATING).notes(UPDATED_NOTES);

        restMedicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedication))
            )
            .andExpect(status().isOk());

        // Validate the Medication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicationUpdatableFieldsEquals(partialUpdatedMedication, getPersistedMedication(partialUpdatedMedication));
    }

    @Test
    @Transactional
    void patchNonExistingMedication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medication.setId(longCount.incrementAndGet());

        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medication.setId(longCount.incrementAndGet());

        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medication.setId(longCount.incrementAndGet());

        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedication() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medication
        restMedicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, medication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicationRepository.count();
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

    protected Medication getPersistedMedication(Medication medication) {
        return medicationRepository.findById(medication.getId()).orElseThrow();
    }

    protected void assertPersistedMedicationToMatchAllProperties(Medication expectedMedication) {
        assertMedicationAllPropertiesEquals(expectedMedication, getPersistedMedication(expectedMedication));
    }

    protected void assertPersistedMedicationToMatchUpdatableProperties(Medication expectedMedication) {
        assertMedicationAllUpdatablePropertiesEquals(expectedMedication, getPersistedMedication(expectedMedication));
    }
}
