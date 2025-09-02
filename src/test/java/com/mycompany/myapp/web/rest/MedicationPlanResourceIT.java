package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MedicationPlanAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MedicationPlan;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.MedicationPlanRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.MedicationPlanService;
import com.mycompany.myapp.service.dto.MedicationPlanDTO;
import com.mycompany.myapp.service.mapper.MedicationPlanMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link MedicationPlanResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MedicationPlanResourceIT {

    private static final String DEFAULT_PLAN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLAN_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_MEDICATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MEDICATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR_HEX = "AAAAAAAAAA";
    private static final String UPDATED_COLOR_HEX = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medication-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicationPlanRepository medicationPlanRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private MedicationPlanRepository medicationPlanRepositoryMock;

    @Autowired
    private MedicationPlanMapper medicationPlanMapper;

    @Mock
    private MedicationPlanService medicationPlanServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicationPlanMockMvc;

    private MedicationPlan medicationPlan;

    private MedicationPlan insertedMedicationPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicationPlan createEntity(EntityManager em) {
        MedicationPlan medicationPlan = new MedicationPlan()
            .planName(DEFAULT_PLAN_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .medicationName(DEFAULT_MEDICATION_NAME)
            .colorHex(DEFAULT_COLOR_HEX);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        medicationPlan.setOwner(user);
        return medicationPlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicationPlan createUpdatedEntity(EntityManager em) {
        MedicationPlan updatedMedicationPlan = new MedicationPlan()
            .planName(UPDATED_PLAN_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .medicationName(UPDATED_MEDICATION_NAME)
            .colorHex(UPDATED_COLOR_HEX);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedMedicationPlan.setOwner(user);
        return updatedMedicationPlan;
    }

    @BeforeEach
    void initTest() {
        medicationPlan = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMedicationPlan != null) {
            medicationPlanRepository.delete(insertedMedicationPlan);
            insertedMedicationPlan = null;
        }
    }

    @Test
    @Transactional
    void createMedicationPlan() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicationPlan
        MedicationPlanDTO medicationPlanDTO = medicationPlanMapper.toDto(medicationPlan);
        var returnedMedicationPlanDTO = om.readValue(
            restMedicationPlanMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationPlanDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicationPlanDTO.class
        );

        // Validate the MedicationPlan in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicationPlan = medicationPlanMapper.toEntity(returnedMedicationPlanDTO);
        assertMedicationPlanUpdatableFieldsEquals(returnedMedicationPlan, getPersistedMedicationPlan(returnedMedicationPlan));

        insertedMedicationPlan = returnedMedicationPlan;
    }

    @Test
    @Transactional
    void createMedicationPlanWithExistingId() throws Exception {
        // Create the MedicationPlan with an existing ID
        medicationPlan.setId(1L);
        MedicationPlanDTO medicationPlanDTO = medicationPlanMapper.toDto(medicationPlan);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicationPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationPlanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicationPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPlanNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicationPlan.setPlanName(null);

        // Create the MedicationPlan, which fails.
        MedicationPlanDTO medicationPlanDTO = medicationPlanMapper.toDto(medicationPlan);

        restMedicationPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicationPlan.setStartDate(null);

        // Create the MedicationPlan, which fails.
        MedicationPlanDTO medicationPlanDTO = medicationPlanMapper.toDto(medicationPlan);

        restMedicationPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicationPlan.setEndDate(null);

        // Create the MedicationPlan, which fails.
        MedicationPlanDTO medicationPlanDTO = medicationPlanMapper.toDto(medicationPlan);

        restMedicationPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMedicationNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicationPlan.setMedicationName(null);

        // Create the MedicationPlan, which fails.
        MedicationPlanDTO medicationPlanDTO = medicationPlanMapper.toDto(medicationPlan);

        restMedicationPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicationPlans() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList
        restMedicationPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicationPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].planName").value(hasItem(DEFAULT_PLAN_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].medicationName").value(hasItem(DEFAULT_MEDICATION_NAME)))
            .andExpect(jsonPath("$.[*].colorHex").value(hasItem(DEFAULT_COLOR_HEX)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicationPlansWithEagerRelationshipsIsEnabled() throws Exception {
        when(medicationPlanServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicationPlanMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(medicationPlanServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicationPlansWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(medicationPlanServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicationPlanMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(medicationPlanRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMedicationPlan() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get the medicationPlan
        restMedicationPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, medicationPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicationPlan.getId().intValue()))
            .andExpect(jsonPath("$.planName").value(DEFAULT_PLAN_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.medicationName").value(DEFAULT_MEDICATION_NAME))
            .andExpect(jsonPath("$.colorHex").value(DEFAULT_COLOR_HEX));
    }

    @Test
    @Transactional
    void getMedicationPlansByIdFiltering() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        Long id = medicationPlan.getId();

        defaultMedicationPlanFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMedicationPlanFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMedicationPlanFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByPlanNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where planName equals to
        defaultMedicationPlanFiltering("planName.equals=" + DEFAULT_PLAN_NAME, "planName.equals=" + UPDATED_PLAN_NAME);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByPlanNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where planName in
        defaultMedicationPlanFiltering("planName.in=" + DEFAULT_PLAN_NAME + "," + UPDATED_PLAN_NAME, "planName.in=" + UPDATED_PLAN_NAME);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByPlanNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where planName is not null
        defaultMedicationPlanFiltering("planName.specified=true", "planName.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicationPlansByPlanNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where planName contains
        defaultMedicationPlanFiltering("planName.contains=" + DEFAULT_PLAN_NAME, "planName.contains=" + UPDATED_PLAN_NAME);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByPlanNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where planName does not contain
        defaultMedicationPlanFiltering("planName.doesNotContain=" + UPDATED_PLAN_NAME, "planName.doesNotContain=" + DEFAULT_PLAN_NAME);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where startDate equals to
        defaultMedicationPlanFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where startDate in
        defaultMedicationPlanFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllMedicationPlansByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where startDate is not null
        defaultMedicationPlanFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicationPlansByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where startDate is greater than or equal to
        defaultMedicationPlanFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllMedicationPlansByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where startDate is less than or equal to
        defaultMedicationPlanFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllMedicationPlansByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where startDate is less than
        defaultMedicationPlanFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where startDate is greater than
        defaultMedicationPlanFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where endDate equals to
        defaultMedicationPlanFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where endDate in
        defaultMedicationPlanFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where endDate is not null
        defaultMedicationPlanFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicationPlansByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where endDate is greater than or equal to
        defaultMedicationPlanFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where endDate is less than or equal to
        defaultMedicationPlanFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where endDate is less than
        defaultMedicationPlanFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where endDate is greater than
        defaultMedicationPlanFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByMedicationNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where medicationName equals to
        defaultMedicationPlanFiltering(
            "medicationName.equals=" + DEFAULT_MEDICATION_NAME,
            "medicationName.equals=" + UPDATED_MEDICATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllMedicationPlansByMedicationNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where medicationName in
        defaultMedicationPlanFiltering(
            "medicationName.in=" + DEFAULT_MEDICATION_NAME + "," + UPDATED_MEDICATION_NAME,
            "medicationName.in=" + UPDATED_MEDICATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllMedicationPlansByMedicationNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where medicationName is not null
        defaultMedicationPlanFiltering("medicationName.specified=true", "medicationName.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicationPlansByMedicationNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where medicationName contains
        defaultMedicationPlanFiltering(
            "medicationName.contains=" + DEFAULT_MEDICATION_NAME,
            "medicationName.contains=" + UPDATED_MEDICATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllMedicationPlansByMedicationNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where medicationName does not contain
        defaultMedicationPlanFiltering(
            "medicationName.doesNotContain=" + UPDATED_MEDICATION_NAME,
            "medicationName.doesNotContain=" + DEFAULT_MEDICATION_NAME
        );
    }

    @Test
    @Transactional
    void getAllMedicationPlansByColorHexIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where colorHex equals to
        defaultMedicationPlanFiltering("colorHex.equals=" + DEFAULT_COLOR_HEX, "colorHex.equals=" + UPDATED_COLOR_HEX);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByColorHexIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where colorHex in
        defaultMedicationPlanFiltering("colorHex.in=" + DEFAULT_COLOR_HEX + "," + UPDATED_COLOR_HEX, "colorHex.in=" + UPDATED_COLOR_HEX);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByColorHexIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where colorHex is not null
        defaultMedicationPlanFiltering("colorHex.specified=true", "colorHex.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicationPlansByColorHexContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where colorHex contains
        defaultMedicationPlanFiltering("colorHex.contains=" + DEFAULT_COLOR_HEX, "colorHex.contains=" + UPDATED_COLOR_HEX);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByColorHexNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        // Get all the medicationPlanList where colorHex does not contain
        defaultMedicationPlanFiltering("colorHex.doesNotContain=" + UPDATED_COLOR_HEX, "colorHex.doesNotContain=" + DEFAULT_COLOR_HEX);
    }

    @Test
    @Transactional
    void getAllMedicationPlansByOwnerIsEqualToSomething() throws Exception {
        User owner;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            medicationPlanRepository.saveAndFlush(medicationPlan);
            owner = UserResourceIT.createEntity();
        } else {
            owner = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(owner);
        em.flush();
        medicationPlan.setOwner(owner);
        medicationPlanRepository.saveAndFlush(medicationPlan);
        Long ownerId = owner.getId();
        // Get all the medicationPlanList where owner equals to ownerId
        defaultMedicationPlanShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the medicationPlanList where owner equals to (ownerId + 1)
        defaultMedicationPlanShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    private void defaultMedicationPlanFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMedicationPlanShouldBeFound(shouldBeFound);
        defaultMedicationPlanShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicationPlanShouldBeFound(String filter) throws Exception {
        restMedicationPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicationPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].planName").value(hasItem(DEFAULT_PLAN_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].medicationName").value(hasItem(DEFAULT_MEDICATION_NAME)))
            .andExpect(jsonPath("$.[*].colorHex").value(hasItem(DEFAULT_COLOR_HEX)));

        // Check, that the count call also returns 1
        restMedicationPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedicationPlanShouldNotBeFound(String filter) throws Exception {
        restMedicationPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicationPlanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedicationPlan() throws Exception {
        // Get the medicationPlan
        restMedicationPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicationPlan() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicationPlan
        MedicationPlan updatedMedicationPlan = medicationPlanRepository.findById(medicationPlan.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicationPlan are not directly saved in db
        em.detach(updatedMedicationPlan);
        updatedMedicationPlan
            .planName(UPDATED_PLAN_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .medicationName(UPDATED_MEDICATION_NAME)
            .colorHex(UPDATED_COLOR_HEX);
        MedicationPlanDTO medicationPlanDTO = medicationPlanMapper.toDto(updatedMedicationPlan);

        restMedicationPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicationPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationPlanDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicationPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicationPlanToMatchAllProperties(updatedMedicationPlan);
    }

    @Test
    @Transactional
    void putNonExistingMedicationPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationPlan.setId(longCount.incrementAndGet());

        // Create the MedicationPlan
        MedicationPlanDTO medicationPlanDTO = medicationPlanMapper.toDto(medicationPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicationPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicationPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicationPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationPlan.setId(longCount.incrementAndGet());

        // Create the MedicationPlan
        MedicationPlanDTO medicationPlanDTO = medicationPlanMapper.toDto(medicationPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicationPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationPlan.setId(longCount.incrementAndGet());

        // Create the MedicationPlan
        MedicationPlanDTO medicationPlanDTO = medicationPlanMapper.toDto(medicationPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicationPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicationPlanWithPatch() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicationPlan using partial update
        MedicationPlan partialUpdatedMedicationPlan = new MedicationPlan();
        partialUpdatedMedicationPlan.setId(medicationPlan.getId());

        partialUpdatedMedicationPlan.endDate(UPDATED_END_DATE).medicationName(UPDATED_MEDICATION_NAME);

        restMedicationPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicationPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicationPlan))
            )
            .andExpect(status().isOk());

        // Validate the MedicationPlan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicationPlanUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicationPlan, medicationPlan),
            getPersistedMedicationPlan(medicationPlan)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicationPlanWithPatch() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicationPlan using partial update
        MedicationPlan partialUpdatedMedicationPlan = new MedicationPlan();
        partialUpdatedMedicationPlan.setId(medicationPlan.getId());

        partialUpdatedMedicationPlan
            .planName(UPDATED_PLAN_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .medicationName(UPDATED_MEDICATION_NAME)
            .colorHex(UPDATED_COLOR_HEX);

        restMedicationPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicationPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicationPlan))
            )
            .andExpect(status().isOk());

        // Validate the MedicationPlan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicationPlanUpdatableFieldsEquals(partialUpdatedMedicationPlan, getPersistedMedicationPlan(partialUpdatedMedicationPlan));
    }

    @Test
    @Transactional
    void patchNonExistingMedicationPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationPlan.setId(longCount.incrementAndGet());

        // Create the MedicationPlan
        MedicationPlanDTO medicationPlanDTO = medicationPlanMapper.toDto(medicationPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicationPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicationPlanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicationPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicationPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationPlan.setId(longCount.incrementAndGet());

        // Create the MedicationPlan
        MedicationPlanDTO medicationPlanDTO = medicationPlanMapper.toDto(medicationPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicationPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicationPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationPlan.setId(longCount.incrementAndGet());

        // Create the MedicationPlan
        MedicationPlanDTO medicationPlanDTO = medicationPlanMapper.toDto(medicationPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationPlanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicationPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicationPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicationPlan() throws Exception {
        // Initialize the database
        insertedMedicationPlan = medicationPlanRepository.saveAndFlush(medicationPlan);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicationPlan
        restMedicationPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicationPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicationPlanRepository.count();
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

    protected MedicationPlan getPersistedMedicationPlan(MedicationPlan medicationPlan) {
        return medicationPlanRepository.findById(medicationPlan.getId()).orElseThrow();
    }

    protected void assertPersistedMedicationPlanToMatchAllProperties(MedicationPlan expectedMedicationPlan) {
        assertMedicationPlanAllPropertiesEquals(expectedMedicationPlan, getPersistedMedicationPlan(expectedMedicationPlan));
    }

    protected void assertPersistedMedicationPlanToMatchUpdatableProperties(MedicationPlan expectedMedicationPlan) {
        assertMedicationPlanAllUpdatablePropertiesEquals(expectedMedicationPlan, getPersistedMedicationPlan(expectedMedicationPlan));
    }
}
