package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MedicationPlanDoseAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MedicationPlan;
import com.mycompany.myapp.domain.MedicationPlanDose;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.MedicationPlanDoseRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.MedicationPlanDoseService;
import com.mycompany.myapp.service.dto.MedicationPlanDoseDTO;
import com.mycompany.myapp.service.mapper.MedicationPlanDoseMapper;
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
 * Integration tests for the {@link MedicationPlanDoseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MedicationPlanDoseResourceIT {

    private static final String DEFAULT_TIME_OF_DAY = "29:13";
    private static final String UPDATED_TIME_OF_DAY = "05:42";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medication-plan-doses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicationPlanDoseRepository medicationPlanDoseRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private MedicationPlanDoseRepository medicationPlanDoseRepositoryMock;

    @Autowired
    private MedicationPlanDoseMapper medicationPlanDoseMapper;

    @Mock
    private MedicationPlanDoseService medicationPlanDoseServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicationPlanDoseMockMvc;

    private MedicationPlanDose medicationPlanDose;

    private MedicationPlanDose insertedMedicationPlanDose;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicationPlanDose createEntity(EntityManager em) {
        MedicationPlanDose medicationPlanDose = new MedicationPlanDose().timeOfDay(DEFAULT_TIME_OF_DAY).notes(DEFAULT_NOTES);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        medicationPlanDose.setOwner(user);
        // Add required entity
        MedicationPlan medicationPlan;
        if (TestUtil.findAll(em, MedicationPlan.class).isEmpty()) {
            medicationPlan = MedicationPlanResourceIT.createEntity(em);
            em.persist(medicationPlan);
            em.flush();
        } else {
            medicationPlan = TestUtil.findAll(em, MedicationPlan.class).get(0);
        }
        medicationPlanDose.setPlan(medicationPlan);
        return medicationPlanDose;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicationPlanDose createUpdatedEntity(EntityManager em) {
        MedicationPlanDose updatedMedicationPlanDose = new MedicationPlanDose().timeOfDay(UPDATED_TIME_OF_DAY).notes(UPDATED_NOTES);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedMedicationPlanDose.setOwner(user);
        // Add required entity
        MedicationPlan medicationPlan;
        if (TestUtil.findAll(em, MedicationPlan.class).isEmpty()) {
            medicationPlan = MedicationPlanResourceIT.createUpdatedEntity(em);
            em.persist(medicationPlan);
            em.flush();
        } else {
            medicationPlan = TestUtil.findAll(em, MedicationPlan.class).get(0);
        }
        updatedMedicationPlanDose.setPlan(medicationPlan);
        return updatedMedicationPlanDose;
    }

    @BeforeEach
    void initTest() {
        medicationPlanDose = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMedicationPlanDose != null) {
            medicationPlanDoseRepository.delete(insertedMedicationPlanDose);
            insertedMedicationPlanDose = null;
        }
    }

    @Test
    @Transactional
    void createMedicationPlanDose() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicationPlanDose
        MedicationPlanDoseDTO medicationPlanDoseDTO = medicationPlanDoseMapper.toDto(medicationPlanDose);
        var returnedMedicationPlanDoseDTO = om.readValue(
            restMedicationPlanDoseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationPlanDoseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicationPlanDoseDTO.class
        );

        // Validate the MedicationPlanDose in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicationPlanDose = medicationPlanDoseMapper.toEntity(returnedMedicationPlanDoseDTO);
        assertMedicationPlanDoseUpdatableFieldsEquals(
            returnedMedicationPlanDose,
            getPersistedMedicationPlanDose(returnedMedicationPlanDose)
        );

        insertedMedicationPlanDose = returnedMedicationPlanDose;
    }

    @Test
    @Transactional
    void createMedicationPlanDoseWithExistingId() throws Exception {
        // Create the MedicationPlanDose with an existing ID
        medicationPlanDose.setId(1L);
        MedicationPlanDoseDTO medicationPlanDoseDTO = medicationPlanDoseMapper.toDto(medicationPlanDose);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicationPlanDoseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationPlanDoseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicationPlanDose in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTimeOfDayIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicationPlanDose.setTimeOfDay(null);

        // Create the MedicationPlanDose, which fails.
        MedicationPlanDoseDTO medicationPlanDoseDTO = medicationPlanDoseMapper.toDto(medicationPlanDose);

        restMedicationPlanDoseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationPlanDoseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicationPlanDoses() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        // Get all the medicationPlanDoseList
        restMedicationPlanDoseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicationPlanDose.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfDay").value(hasItem(DEFAULT_TIME_OF_DAY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicationPlanDosesWithEagerRelationshipsIsEnabled() throws Exception {
        when(medicationPlanDoseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicationPlanDoseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(medicationPlanDoseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicationPlanDosesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(medicationPlanDoseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicationPlanDoseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(medicationPlanDoseRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMedicationPlanDose() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        // Get the medicationPlanDose
        restMedicationPlanDoseMockMvc
            .perform(get(ENTITY_API_URL_ID, medicationPlanDose.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicationPlanDose.getId().intValue()))
            .andExpect(jsonPath("$.timeOfDay").value(DEFAULT_TIME_OF_DAY))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getMedicationPlanDosesByIdFiltering() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        Long id = medicationPlanDose.getId();

        defaultMedicationPlanDoseFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMedicationPlanDoseFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMedicationPlanDoseFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMedicationPlanDosesByTimeOfDayIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        // Get all the medicationPlanDoseList where timeOfDay equals to
        defaultMedicationPlanDoseFiltering("timeOfDay.equals=" + DEFAULT_TIME_OF_DAY, "timeOfDay.equals=" + UPDATED_TIME_OF_DAY);
    }

    @Test
    @Transactional
    void getAllMedicationPlanDosesByTimeOfDayIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        // Get all the medicationPlanDoseList where timeOfDay in
        defaultMedicationPlanDoseFiltering(
            "timeOfDay.in=" + DEFAULT_TIME_OF_DAY + "," + UPDATED_TIME_OF_DAY,
            "timeOfDay.in=" + UPDATED_TIME_OF_DAY
        );
    }

    @Test
    @Transactional
    void getAllMedicationPlanDosesByTimeOfDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        // Get all the medicationPlanDoseList where timeOfDay is not null
        defaultMedicationPlanDoseFiltering("timeOfDay.specified=true", "timeOfDay.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicationPlanDosesByTimeOfDayContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        // Get all the medicationPlanDoseList where timeOfDay contains
        defaultMedicationPlanDoseFiltering("timeOfDay.contains=" + DEFAULT_TIME_OF_DAY, "timeOfDay.contains=" + UPDATED_TIME_OF_DAY);
    }

    @Test
    @Transactional
    void getAllMedicationPlanDosesByTimeOfDayNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        // Get all the medicationPlanDoseList where timeOfDay does not contain
        defaultMedicationPlanDoseFiltering(
            "timeOfDay.doesNotContain=" + UPDATED_TIME_OF_DAY,
            "timeOfDay.doesNotContain=" + DEFAULT_TIME_OF_DAY
        );
    }

    @Test
    @Transactional
    void getAllMedicationPlanDosesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        // Get all the medicationPlanDoseList where notes equals to
        defaultMedicationPlanDoseFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllMedicationPlanDosesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        // Get all the medicationPlanDoseList where notes in
        defaultMedicationPlanDoseFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllMedicationPlanDosesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        // Get all the medicationPlanDoseList where notes is not null
        defaultMedicationPlanDoseFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicationPlanDosesByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        // Get all the medicationPlanDoseList where notes contains
        defaultMedicationPlanDoseFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllMedicationPlanDosesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        // Get all the medicationPlanDoseList where notes does not contain
        defaultMedicationPlanDoseFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllMedicationPlanDosesByOwnerIsEqualToSomething() throws Exception {
        User owner;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);
            owner = UserResourceIT.createEntity();
        } else {
            owner = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(owner);
        em.flush();
        medicationPlanDose.setOwner(owner);
        medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);
        Long ownerId = owner.getId();
        // Get all the medicationPlanDoseList where owner equals to ownerId
        defaultMedicationPlanDoseShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the medicationPlanDoseList where owner equals to (ownerId + 1)
        defaultMedicationPlanDoseShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    @Test
    @Transactional
    void getAllMedicationPlanDosesByPlanIsEqualToSomething() throws Exception {
        MedicationPlan plan;
        if (TestUtil.findAll(em, MedicationPlan.class).isEmpty()) {
            medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);
            plan = MedicationPlanResourceIT.createEntity(em);
        } else {
            plan = TestUtil.findAll(em, MedicationPlan.class).get(0);
        }
        em.persist(plan);
        em.flush();
        medicationPlanDose.setPlan(plan);
        medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);
        Long planId = plan.getId();
        // Get all the medicationPlanDoseList where plan equals to planId
        defaultMedicationPlanDoseShouldBeFound("planId.equals=" + planId);

        // Get all the medicationPlanDoseList where plan equals to (planId + 1)
        defaultMedicationPlanDoseShouldNotBeFound("planId.equals=" + (planId + 1));
    }

    private void defaultMedicationPlanDoseFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMedicationPlanDoseShouldBeFound(shouldBeFound);
        defaultMedicationPlanDoseShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicationPlanDoseShouldBeFound(String filter) throws Exception {
        restMedicationPlanDoseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicationPlanDose.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfDay").value(hasItem(DEFAULT_TIME_OF_DAY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restMedicationPlanDoseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedicationPlanDoseShouldNotBeFound(String filter) throws Exception {
        restMedicationPlanDoseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicationPlanDoseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedicationPlanDose() throws Exception {
        // Get the medicationPlanDose
        restMedicationPlanDoseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicationPlanDose() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicationPlanDose
        MedicationPlanDose updatedMedicationPlanDose = medicationPlanDoseRepository.findById(medicationPlanDose.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicationPlanDose are not directly saved in db
        em.detach(updatedMedicationPlanDose);
        updatedMedicationPlanDose.timeOfDay(UPDATED_TIME_OF_DAY).notes(UPDATED_NOTES);
        MedicationPlanDoseDTO medicationPlanDoseDTO = medicationPlanDoseMapper.toDto(updatedMedicationPlanDose);

        restMedicationPlanDoseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicationPlanDoseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationPlanDoseDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicationPlanDose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicationPlanDoseToMatchAllProperties(updatedMedicationPlanDose);
    }

    @Test
    @Transactional
    void putNonExistingMedicationPlanDose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationPlanDose.setId(longCount.incrementAndGet());

        // Create the MedicationPlanDose
        MedicationPlanDoseDTO medicationPlanDoseDTO = medicationPlanDoseMapper.toDto(medicationPlanDose);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicationPlanDoseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicationPlanDoseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationPlanDoseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationPlanDose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicationPlanDose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationPlanDose.setId(longCount.incrementAndGet());

        // Create the MedicationPlanDose
        MedicationPlanDoseDTO medicationPlanDoseDTO = medicationPlanDoseMapper.toDto(medicationPlanDose);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationPlanDoseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationPlanDoseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationPlanDose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicationPlanDose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationPlanDose.setId(longCount.incrementAndGet());

        // Create the MedicationPlanDose
        MedicationPlanDoseDTO medicationPlanDoseDTO = medicationPlanDoseMapper.toDto(medicationPlanDose);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationPlanDoseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationPlanDoseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicationPlanDose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicationPlanDoseWithPatch() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicationPlanDose using partial update
        MedicationPlanDose partialUpdatedMedicationPlanDose = new MedicationPlanDose();
        partialUpdatedMedicationPlanDose.setId(medicationPlanDose.getId());

        partialUpdatedMedicationPlanDose.timeOfDay(UPDATED_TIME_OF_DAY).notes(UPDATED_NOTES);

        restMedicationPlanDoseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicationPlanDose.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicationPlanDose))
            )
            .andExpect(status().isOk());

        // Validate the MedicationPlanDose in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicationPlanDoseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicationPlanDose, medicationPlanDose),
            getPersistedMedicationPlanDose(medicationPlanDose)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicationPlanDoseWithPatch() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicationPlanDose using partial update
        MedicationPlanDose partialUpdatedMedicationPlanDose = new MedicationPlanDose();
        partialUpdatedMedicationPlanDose.setId(medicationPlanDose.getId());

        partialUpdatedMedicationPlanDose.timeOfDay(UPDATED_TIME_OF_DAY).notes(UPDATED_NOTES);

        restMedicationPlanDoseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicationPlanDose.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicationPlanDose))
            )
            .andExpect(status().isOk());

        // Validate the MedicationPlanDose in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicationPlanDoseUpdatableFieldsEquals(
            partialUpdatedMedicationPlanDose,
            getPersistedMedicationPlanDose(partialUpdatedMedicationPlanDose)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMedicationPlanDose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationPlanDose.setId(longCount.incrementAndGet());

        // Create the MedicationPlanDose
        MedicationPlanDoseDTO medicationPlanDoseDTO = medicationPlanDoseMapper.toDto(medicationPlanDose);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicationPlanDoseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicationPlanDoseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicationPlanDoseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationPlanDose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicationPlanDose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationPlanDose.setId(longCount.incrementAndGet());

        // Create the MedicationPlanDose
        MedicationPlanDoseDTO medicationPlanDoseDTO = medicationPlanDoseMapper.toDto(medicationPlanDose);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationPlanDoseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicationPlanDoseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationPlanDose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicationPlanDose() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationPlanDose.setId(longCount.incrementAndGet());

        // Create the MedicationPlanDose
        MedicationPlanDoseDTO medicationPlanDoseDTO = medicationPlanDoseMapper.toDto(medicationPlanDose);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationPlanDoseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicationPlanDoseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicationPlanDose in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicationPlanDose() throws Exception {
        // Initialize the database
        insertedMedicationPlanDose = medicationPlanDoseRepository.saveAndFlush(medicationPlanDose);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicationPlanDose
        restMedicationPlanDoseMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicationPlanDose.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicationPlanDoseRepository.count();
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

    protected MedicationPlanDose getPersistedMedicationPlanDose(MedicationPlanDose medicationPlanDose) {
        return medicationPlanDoseRepository.findById(medicationPlanDose.getId()).orElseThrow();
    }

    protected void assertPersistedMedicationPlanDoseToMatchAllProperties(MedicationPlanDose expectedMedicationPlanDose) {
        assertMedicationPlanDoseAllPropertiesEquals(expectedMedicationPlanDose, getPersistedMedicationPlanDose(expectedMedicationPlanDose));
    }

    protected void assertPersistedMedicationPlanDoseToMatchUpdatableProperties(MedicationPlanDose expectedMedicationPlanDose) {
        assertMedicationPlanDoseAllUpdatablePropertiesEquals(
            expectedMedicationPlanDose,
            getPersistedMedicationPlanDose(expectedMedicationPlanDose)
        );
    }
}
