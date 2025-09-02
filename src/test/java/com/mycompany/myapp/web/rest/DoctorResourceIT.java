package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DoctorAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.DoctorService;
import com.mycompany.myapp.service.dto.DoctorDTO;
import com.mycompany.myapp.service.mapper.DoctorMapper;
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
 * Integration tests for the {@link DoctorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DoctorResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIALTY = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALTY = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doctors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private DoctorRepository doctorRepositoryMock;

    @Autowired
    private DoctorMapper doctorMapper;

    @Mock
    private DoctorService doctorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoctorMockMvc;

    private Doctor doctor;

    private Doctor insertedDoctor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctor createEntity(EntityManager em) {
        Doctor doctor = new Doctor()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .specialty(DEFAULT_SPECIALTY)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .notes(DEFAULT_NOTES);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        doctor.setOwner(user);
        return doctor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctor createUpdatedEntity(EntityManager em) {
        Doctor updatedDoctor = new Doctor()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .specialty(UPDATED_SPECIALTY)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .notes(UPDATED_NOTES);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedDoctor.setOwner(user);
        return updatedDoctor;
    }

    @BeforeEach
    void initTest() {
        doctor = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDoctor != null) {
            doctorRepository.delete(insertedDoctor);
            insertedDoctor = null;
        }
    }

    @Test
    @Transactional
    void createDoctor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);
        var returnedDoctorDTO = om.readValue(
            restDoctorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DoctorDTO.class
        );

        // Validate the Doctor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDoctor = doctorMapper.toEntity(returnedDoctorDTO);
        assertDoctorUpdatableFieldsEquals(returnedDoctor, getPersistedDoctor(returnedDoctor));

        insertedDoctor = returnedDoctor;
    }

    @Test
    @Transactional
    void createDoctorWithExistingId() throws Exception {
        // Create the Doctor with an existing ID
        doctor.setId(1L);
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctor.setFirstName(null);

        // Create the Doctor, which fails.
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctor.setLastName(null);

        // Create the Doctor, which fails.
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpecialtyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctor.setSpecialty(null);

        // Create the Doctor, which fails.
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDoctors() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].specialty").value(hasItem(DEFAULT_SPECIALTY)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDoctorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(doctorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDoctorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(doctorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDoctorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(doctorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDoctorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(doctorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDoctor() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get the doctor
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL_ID, doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doctor.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.specialty").value(DEFAULT_SPECIALTY))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getDoctorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        Long id = doctor.getId();

        defaultDoctorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDoctorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDoctorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName equals to
        defaultDoctorFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName in
        defaultDoctorFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName is not null
        defaultDoctorFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName contains
        defaultDoctorFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName does not contain
        defaultDoctorFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName equals to
        defaultDoctorFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName in
        defaultDoctorFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName is not null
        defaultDoctorFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName contains
        defaultDoctorFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName does not contain
        defaultDoctorFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecialtyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialty equals to
        defaultDoctorFiltering("specialty.equals=" + DEFAULT_SPECIALTY, "specialty.equals=" + UPDATED_SPECIALTY);
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecialtyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialty in
        defaultDoctorFiltering("specialty.in=" + DEFAULT_SPECIALTY + "," + UPDATED_SPECIALTY, "specialty.in=" + UPDATED_SPECIALTY);
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecialtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialty is not null
        defaultDoctorFiltering("specialty.specified=true", "specialty.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecialtyContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialty contains
        defaultDoctorFiltering("specialty.contains=" + DEFAULT_SPECIALTY, "specialty.contains=" + UPDATED_SPECIALTY);
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecialtyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialty does not contain
        defaultDoctorFiltering("specialty.doesNotContain=" + UPDATED_SPECIALTY, "specialty.doesNotContain=" + DEFAULT_SPECIALTY);
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone equals to
        defaultDoctorFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone in
        defaultDoctorFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone is not null
        defaultDoctorFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone contains
        defaultDoctorFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone does not contain
        defaultDoctorFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where address equals to
        defaultDoctorFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDoctorsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where address in
        defaultDoctorFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDoctorsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where address is not null
        defaultDoctorFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where address contains
        defaultDoctorFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDoctorsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where address does not contain
        defaultDoctorFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDoctorsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where notes equals to
        defaultDoctorFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDoctorsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where notes in
        defaultDoctorFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDoctorsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where notes is not null
        defaultDoctorFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where notes contains
        defaultDoctorFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDoctorsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where notes does not contain
        defaultDoctorFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllDoctorsByOwnerIsEqualToSomething() throws Exception {
        User owner;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            doctorRepository.saveAndFlush(doctor);
            owner = UserResourceIT.createEntity();
        } else {
            owner = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(owner);
        em.flush();
        doctor.setOwner(owner);
        doctorRepository.saveAndFlush(doctor);
        Long ownerId = owner.getId();
        // Get all the doctorList where owner equals to ownerId
        defaultDoctorShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the doctorList where owner equals to (ownerId + 1)
        defaultDoctorShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    private void defaultDoctorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDoctorShouldBeFound(shouldBeFound);
        defaultDoctorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDoctorShouldBeFound(String filter) throws Exception {
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].specialty").value(hasItem(DEFAULT_SPECIALTY)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDoctorShouldNotBeFound(String filter) throws Exception {
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDoctor() throws Exception {
        // Get the doctor
        restDoctorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDoctor() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the doctor
        Doctor updatedDoctor = doctorRepository.findById(doctor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDoctor are not directly saved in db
        em.detach(updatedDoctor);
        updatedDoctor
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .specialty(UPDATED_SPECIALTY)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .notes(UPDATED_NOTES);
        DoctorDTO doctorDTO = doctorMapper.toDto(updatedDoctor);

        restDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDoctorToMatchAllProperties(updatedDoctor);
    }

    @Test
    @Transactional
    void putNonExistingDoctor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctor.setId(longCount.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDoctor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctor.setId(longCount.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDoctor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctor.setId(longCount.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDoctorWithPatch() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the doctor using partial update
        Doctor partialUpdatedDoctor = new Doctor();
        partialUpdatedDoctor.setId(doctor.getId());

        partialUpdatedDoctor.specialty(UPDATED_SPECIALTY).phone(UPDATED_PHONE).address(UPDATED_ADDRESS);

        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDoctor))
            )
            .andExpect(status().isOk());

        // Validate the Doctor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDoctorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDoctor, doctor), getPersistedDoctor(doctor));
    }

    @Test
    @Transactional
    void fullUpdateDoctorWithPatch() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the doctor using partial update
        Doctor partialUpdatedDoctor = new Doctor();
        partialUpdatedDoctor.setId(doctor.getId());

        partialUpdatedDoctor
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .specialty(UPDATED_SPECIALTY)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .notes(UPDATED_NOTES);

        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDoctor))
            )
            .andExpect(status().isOk());

        // Validate the Doctor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDoctorUpdatableFieldsEquals(partialUpdatedDoctor, getPersistedDoctor(partialUpdatedDoctor));
    }

    @Test
    @Transactional
    void patchNonExistingDoctor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctor.setId(longCount.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, doctorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDoctor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctor.setId(longCount.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDoctor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctor.setId(longCount.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(doctorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doctor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDoctor() throws Exception {
        // Initialize the database
        insertedDoctor = doctorRepository.saveAndFlush(doctor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the doctor
        restDoctorMockMvc
            .perform(delete(ENTITY_API_URL_ID, doctor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return doctorRepository.count();
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

    protected Doctor getPersistedDoctor(Doctor doctor) {
        return doctorRepository.findById(doctor.getId()).orElseThrow();
    }

    protected void assertPersistedDoctorToMatchAllProperties(Doctor expectedDoctor) {
        assertDoctorAllPropertiesEquals(expectedDoctor, getPersistedDoctor(expectedDoctor));
    }

    protected void assertPersistedDoctorToMatchUpdatableProperties(Doctor expectedDoctor) {
        assertDoctorAllUpdatablePropertiesEquals(expectedDoctor, getPersistedDoctor(expectedDoctor));
    }
}
