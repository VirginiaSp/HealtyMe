package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ExaminationRecordAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ExaminationCategory;
import com.mycompany.myapp.domain.ExaminationRecord;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.ExaminationRecordRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.ExaminationRecordService;
import com.mycompany.myapp.service.dto.ExaminationRecordDTO;
import com.mycompany.myapp.service.mapper.ExaminationRecordMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
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
 * Integration tests for the {@link ExaminationRecordResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExaminationRecordResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXAM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXAM_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXAM_DATE = LocalDate.ofEpochDay(-1L);

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ORIGINAL_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_FILENAME = "BBBBBBBBBB";

    private static final String DEFAULT_STORED_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_STORED_FILENAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/examination-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExaminationRecordRepository examinationRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ExaminationRecordRepository examinationRecordRepositoryMock;

    @Autowired
    private ExaminationRecordMapper examinationRecordMapper;

    @Mock
    private ExaminationRecordService examinationRecordServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExaminationRecordMockMvc;

    private ExaminationRecord examinationRecord;

    private ExaminationRecord insertedExaminationRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExaminationRecord createEntity(EntityManager em) {
        ExaminationRecord examinationRecord = new ExaminationRecord()
            .title(DEFAULT_TITLE)
            .examDate(DEFAULT_EXAM_DATE)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .originalFilename(DEFAULT_ORIGINAL_FILENAME)
            .storedFilename(DEFAULT_STORED_FILENAME)
            .notes(DEFAULT_NOTES);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        examinationRecord.setOwner(user);
        return examinationRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExaminationRecord createUpdatedEntity(EntityManager em) {
        ExaminationRecord updatedExaminationRecord = new ExaminationRecord()
            .title(UPDATED_TITLE)
            .examDate(UPDATED_EXAM_DATE)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .originalFilename(UPDATED_ORIGINAL_FILENAME)
            .storedFilename(UPDATED_STORED_FILENAME)
            .notes(UPDATED_NOTES);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedExaminationRecord.setOwner(user);
        return updatedExaminationRecord;
    }

    @BeforeEach
    void initTest() {
        examinationRecord = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedExaminationRecord != null) {
            examinationRecordRepository.delete(insertedExaminationRecord);
            insertedExaminationRecord = null;
        }
    }

    @Test
    @Transactional
    void createExaminationRecord() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExaminationRecord
        ExaminationRecordDTO examinationRecordDTO = examinationRecordMapper.toDto(examinationRecord);
        var returnedExaminationRecordDTO = om.readValue(
            restExaminationRecordMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationRecordDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExaminationRecordDTO.class
        );

        // Validate the ExaminationRecord in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExaminationRecord = examinationRecordMapper.toEntity(returnedExaminationRecordDTO);
        assertExaminationRecordUpdatableFieldsEquals(returnedExaminationRecord, getPersistedExaminationRecord(returnedExaminationRecord));

        insertedExaminationRecord = returnedExaminationRecord;
    }

    @Test
    @Transactional
    void createExaminationRecordWithExistingId() throws Exception {
        // Create the ExaminationRecord with an existing ID
        examinationRecord.setId(1L);
        ExaminationRecordDTO examinationRecordDTO = examinationRecordMapper.toDto(examinationRecord);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExaminationRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExaminationRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examinationRecord.setTitle(null);

        // Create the ExaminationRecord, which fails.
        ExaminationRecordDTO examinationRecordDTO = examinationRecordMapper.toDto(examinationRecord);

        restExaminationRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExamDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examinationRecord.setExamDate(null);

        // Create the ExaminationRecord, which fails.
        ExaminationRecordDTO examinationRecordDTO = examinationRecordMapper.toDto(examinationRecord);

        restExaminationRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExaminationRecords() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList
        restExaminationRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examinationRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].examDate").value(hasItem(DEFAULT_EXAM_DATE.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].originalFilename").value(hasItem(DEFAULT_ORIGINAL_FILENAME)))
            .andExpect(jsonPath("$.[*].storedFilename").value(hasItem(DEFAULT_STORED_FILENAME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExaminationRecordsWithEagerRelationshipsIsEnabled() throws Exception {
        when(examinationRecordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExaminationRecordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(examinationRecordServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExaminationRecordsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(examinationRecordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExaminationRecordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(examinationRecordRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getExaminationRecord() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get the examinationRecord
        restExaminationRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, examinationRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examinationRecord.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.examDate").value(DEFAULT_EXAM_DATE.toString()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64.getEncoder().encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.originalFilename").value(DEFAULT_ORIGINAL_FILENAME))
            .andExpect(jsonPath("$.storedFilename").value(DEFAULT_STORED_FILENAME))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getExaminationRecordsByIdFiltering() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        Long id = examinationRecord.getId();

        defaultExaminationRecordFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultExaminationRecordFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultExaminationRecordFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where title equals to
        defaultExaminationRecordFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where title in
        defaultExaminationRecordFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where title is not null
        defaultExaminationRecordFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where title contains
        defaultExaminationRecordFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where title does not contain
        defaultExaminationRecordFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByExamDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where examDate equals to
        defaultExaminationRecordFiltering("examDate.equals=" + DEFAULT_EXAM_DATE, "examDate.equals=" + UPDATED_EXAM_DATE);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByExamDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where examDate in
        defaultExaminationRecordFiltering("examDate.in=" + DEFAULT_EXAM_DATE + "," + UPDATED_EXAM_DATE, "examDate.in=" + UPDATED_EXAM_DATE);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByExamDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where examDate is not null
        defaultExaminationRecordFiltering("examDate.specified=true", "examDate.specified=false");
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByExamDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where examDate is greater than or equal to
        defaultExaminationRecordFiltering(
            "examDate.greaterThanOrEqual=" + DEFAULT_EXAM_DATE,
            "examDate.greaterThanOrEqual=" + UPDATED_EXAM_DATE
        );
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByExamDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where examDate is less than or equal to
        defaultExaminationRecordFiltering("examDate.lessThanOrEqual=" + DEFAULT_EXAM_DATE, "examDate.lessThanOrEqual=" + SMALLER_EXAM_DATE);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByExamDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where examDate is less than
        defaultExaminationRecordFiltering("examDate.lessThan=" + UPDATED_EXAM_DATE, "examDate.lessThan=" + DEFAULT_EXAM_DATE);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByExamDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where examDate is greater than
        defaultExaminationRecordFiltering("examDate.greaterThan=" + SMALLER_EXAM_DATE, "examDate.greaterThan=" + DEFAULT_EXAM_DATE);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByOriginalFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where originalFilename equals to
        defaultExaminationRecordFiltering(
            "originalFilename.equals=" + DEFAULT_ORIGINAL_FILENAME,
            "originalFilename.equals=" + UPDATED_ORIGINAL_FILENAME
        );
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByOriginalFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where originalFilename in
        defaultExaminationRecordFiltering(
            "originalFilename.in=" + DEFAULT_ORIGINAL_FILENAME + "," + UPDATED_ORIGINAL_FILENAME,
            "originalFilename.in=" + UPDATED_ORIGINAL_FILENAME
        );
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByOriginalFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where originalFilename is not null
        defaultExaminationRecordFiltering("originalFilename.specified=true", "originalFilename.specified=false");
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByOriginalFilenameContainsSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where originalFilename contains
        defaultExaminationRecordFiltering(
            "originalFilename.contains=" + DEFAULT_ORIGINAL_FILENAME,
            "originalFilename.contains=" + UPDATED_ORIGINAL_FILENAME
        );
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByOriginalFilenameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where originalFilename does not contain
        defaultExaminationRecordFiltering(
            "originalFilename.doesNotContain=" + UPDATED_ORIGINAL_FILENAME,
            "originalFilename.doesNotContain=" + DEFAULT_ORIGINAL_FILENAME
        );
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByStoredFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where storedFilename equals to
        defaultExaminationRecordFiltering(
            "storedFilename.equals=" + DEFAULT_STORED_FILENAME,
            "storedFilename.equals=" + UPDATED_STORED_FILENAME
        );
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByStoredFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where storedFilename in
        defaultExaminationRecordFiltering(
            "storedFilename.in=" + DEFAULT_STORED_FILENAME + "," + UPDATED_STORED_FILENAME,
            "storedFilename.in=" + UPDATED_STORED_FILENAME
        );
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByStoredFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where storedFilename is not null
        defaultExaminationRecordFiltering("storedFilename.specified=true", "storedFilename.specified=false");
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByStoredFilenameContainsSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where storedFilename contains
        defaultExaminationRecordFiltering(
            "storedFilename.contains=" + DEFAULT_STORED_FILENAME,
            "storedFilename.contains=" + UPDATED_STORED_FILENAME
        );
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByStoredFilenameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where storedFilename does not contain
        defaultExaminationRecordFiltering(
            "storedFilename.doesNotContain=" + UPDATED_STORED_FILENAME,
            "storedFilename.doesNotContain=" + DEFAULT_STORED_FILENAME
        );
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where notes equals to
        defaultExaminationRecordFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where notes in
        defaultExaminationRecordFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where notes is not null
        defaultExaminationRecordFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where notes contains
        defaultExaminationRecordFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        // Get all the examinationRecordList where notes does not contain
        defaultExaminationRecordFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByOwnerIsEqualToSomething() throws Exception {
        User owner;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            examinationRecordRepository.saveAndFlush(examinationRecord);
            owner = UserResourceIT.createEntity();
        } else {
            owner = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(owner);
        em.flush();
        examinationRecord.setOwner(owner);
        examinationRecordRepository.saveAndFlush(examinationRecord);
        Long ownerId = owner.getId();
        // Get all the examinationRecordList where owner equals to ownerId
        defaultExaminationRecordShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the examinationRecordList where owner equals to (ownerId + 1)
        defaultExaminationRecordShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    @Test
    @Transactional
    void getAllExaminationRecordsByCategoryIsEqualToSomething() throws Exception {
        ExaminationCategory category;
        if (TestUtil.findAll(em, ExaminationCategory.class).isEmpty()) {
            examinationRecordRepository.saveAndFlush(examinationRecord);
            category = ExaminationCategoryResourceIT.createEntity(em);
        } else {
            category = TestUtil.findAll(em, ExaminationCategory.class).get(0);
        }
        em.persist(category);
        em.flush();
        examinationRecord.setCategory(category);
        examinationRecordRepository.saveAndFlush(examinationRecord);
        Long categoryId = category.getId();
        // Get all the examinationRecordList where category equals to categoryId
        defaultExaminationRecordShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the examinationRecordList where category equals to (categoryId + 1)
        defaultExaminationRecordShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    private void defaultExaminationRecordFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultExaminationRecordShouldBeFound(shouldBeFound);
        defaultExaminationRecordShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExaminationRecordShouldBeFound(String filter) throws Exception {
        restExaminationRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examinationRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].examDate").value(hasItem(DEFAULT_EXAM_DATE.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].originalFilename").value(hasItem(DEFAULT_ORIGINAL_FILENAME)))
            .andExpect(jsonPath("$.[*].storedFilename").value(hasItem(DEFAULT_STORED_FILENAME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restExaminationRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExaminationRecordShouldNotBeFound(String filter) throws Exception {
        restExaminationRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExaminationRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExaminationRecord() throws Exception {
        // Get the examinationRecord
        restExaminationRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExaminationRecord() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examinationRecord
        ExaminationRecord updatedExaminationRecord = examinationRecordRepository.findById(examinationRecord.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExaminationRecord are not directly saved in db
        em.detach(updatedExaminationRecord);
        updatedExaminationRecord
            .title(UPDATED_TITLE)
            .examDate(UPDATED_EXAM_DATE)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .originalFilename(UPDATED_ORIGINAL_FILENAME)
            .storedFilename(UPDATED_STORED_FILENAME)
            .notes(UPDATED_NOTES);
        ExaminationRecordDTO examinationRecordDTO = examinationRecordMapper.toDto(updatedExaminationRecord);

        restExaminationRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examinationRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examinationRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExaminationRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExaminationRecordToMatchAllProperties(updatedExaminationRecord);
    }

    @Test
    @Transactional
    void putNonExistingExaminationRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationRecord.setId(longCount.incrementAndGet());

        // Create the ExaminationRecord
        ExaminationRecordDTO examinationRecordDTO = examinationRecordMapper.toDto(examinationRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExaminationRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examinationRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examinationRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExaminationRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationRecord.setId(longCount.incrementAndGet());

        // Create the ExaminationRecord
        ExaminationRecordDTO examinationRecordDTO = examinationRecordMapper.toDto(examinationRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examinationRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExaminationRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationRecord.setId(longCount.incrementAndGet());

        // Create the ExaminationRecord
        ExaminationRecordDTO examinationRecordDTO = examinationRecordMapper.toDto(examinationRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExaminationRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExaminationRecordWithPatch() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examinationRecord using partial update
        ExaminationRecord partialUpdatedExaminationRecord = new ExaminationRecord();
        partialUpdatedExaminationRecord.setId(examinationRecord.getId());

        partialUpdatedExaminationRecord.file(UPDATED_FILE).fileContentType(UPDATED_FILE_CONTENT_TYPE);

        restExaminationRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExaminationRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExaminationRecord))
            )
            .andExpect(status().isOk());

        // Validate the ExaminationRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExaminationRecordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExaminationRecord, examinationRecord),
            getPersistedExaminationRecord(examinationRecord)
        );
    }

    @Test
    @Transactional
    void fullUpdateExaminationRecordWithPatch() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examinationRecord using partial update
        ExaminationRecord partialUpdatedExaminationRecord = new ExaminationRecord();
        partialUpdatedExaminationRecord.setId(examinationRecord.getId());

        partialUpdatedExaminationRecord
            .title(UPDATED_TITLE)
            .examDate(UPDATED_EXAM_DATE)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .originalFilename(UPDATED_ORIGINAL_FILENAME)
            .storedFilename(UPDATED_STORED_FILENAME)
            .notes(UPDATED_NOTES);

        restExaminationRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExaminationRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExaminationRecord))
            )
            .andExpect(status().isOk());

        // Validate the ExaminationRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExaminationRecordUpdatableFieldsEquals(
            partialUpdatedExaminationRecord,
            getPersistedExaminationRecord(partialUpdatedExaminationRecord)
        );
    }

    @Test
    @Transactional
    void patchNonExistingExaminationRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationRecord.setId(longCount.incrementAndGet());

        // Create the ExaminationRecord
        ExaminationRecordDTO examinationRecordDTO = examinationRecordMapper.toDto(examinationRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExaminationRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examinationRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(examinationRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExaminationRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationRecord.setId(longCount.incrementAndGet());

        // Create the ExaminationRecord
        ExaminationRecordDTO examinationRecordDTO = examinationRecordMapper.toDto(examinationRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(examinationRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExaminationRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationRecord.setId(longCount.incrementAndGet());

        // Create the ExaminationRecord
        ExaminationRecordDTO examinationRecordDTO = examinationRecordMapper.toDto(examinationRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationRecordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(examinationRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExaminationRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExaminationRecord() throws Exception {
        // Initialize the database
        insertedExaminationRecord = examinationRecordRepository.saveAndFlush(examinationRecord);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the examinationRecord
        restExaminationRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, examinationRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return examinationRecordRepository.count();
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

    protected ExaminationRecord getPersistedExaminationRecord(ExaminationRecord examinationRecord) {
        return examinationRecordRepository.findById(examinationRecord.getId()).orElseThrow();
    }

    protected void assertPersistedExaminationRecordToMatchAllProperties(ExaminationRecord expectedExaminationRecord) {
        assertExaminationRecordAllPropertiesEquals(expectedExaminationRecord, getPersistedExaminationRecord(expectedExaminationRecord));
    }

    protected void assertPersistedExaminationRecordToMatchUpdatableProperties(ExaminationRecord expectedExaminationRecord) {
        assertExaminationRecordAllUpdatablePropertiesEquals(
            expectedExaminationRecord,
            getPersistedExaminationRecord(expectedExaminationRecord)
        );
    }
}
