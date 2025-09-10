package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MedicationCategoryAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MedicationCategory;
import com.mycompany.myapp.repository.MedicationCategoryRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.MedicationCategoryService;
import com.mycompany.myapp.service.dto.MedicationCategoryDTO;
import com.mycompany.myapp.service.mapper.MedicationCategoryMapper;
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
 * Integration tests for the {@link MedicationCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MedicationCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/medication-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicationCategoryRepository medicationCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private MedicationCategoryRepository medicationCategoryRepositoryMock;

    @Autowired
    private MedicationCategoryMapper medicationCategoryMapper;

    @Mock
    private MedicationCategoryService medicationCategoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicationCategoryMockMvc;

    private MedicationCategory medicationCategory;

    private MedicationCategory insertedMedicationCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicationCategory createEntity() {
        return new MedicationCategory()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .color(DEFAULT_COLOR)
            .icon(DEFAULT_ICON)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicationCategory createUpdatedEntity() {
        return new MedicationCategory()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .color(UPDATED_COLOR)
            .icon(UPDATED_ICON)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        medicationCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMedicationCategory != null) {
            medicationCategoryRepository.delete(insertedMedicationCategory);
            insertedMedicationCategory = null;
        }
    }

    @Test
    @Transactional
    void createMedicationCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicationCategory
        MedicationCategoryDTO medicationCategoryDTO = medicationCategoryMapper.toDto(medicationCategory);
        var returnedMedicationCategoryDTO = om.readValue(
            restMedicationCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicationCategoryDTO.class
        );

        // Validate the MedicationCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicationCategory = medicationCategoryMapper.toEntity(returnedMedicationCategoryDTO);
        assertMedicationCategoryUpdatableFieldsEquals(
            returnedMedicationCategory,
            getPersistedMedicationCategory(returnedMedicationCategory)
        );

        insertedMedicationCategory = returnedMedicationCategory;
    }

    @Test
    @Transactional
    void createMedicationCategoryWithExistingId() throws Exception {
        // Create the MedicationCategory with an existing ID
        medicationCategory.setId(1L);
        MedicationCategoryDTO medicationCategoryDTO = medicationCategoryMapper.toDto(medicationCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicationCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicationCategory.setName(null);

        // Create the MedicationCategory, which fails.
        MedicationCategoryDTO medicationCategoryDTO = medicationCategoryMapper.toDto(medicationCategory);

        restMedicationCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicationCategories() throws Exception {
        // Initialize the database
        insertedMedicationCategory = medicationCategoryRepository.saveAndFlush(medicationCategory);

        // Get all the medicationCategoryList
        restMedicationCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicationCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicationCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(medicationCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicationCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(medicationCategoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicationCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(medicationCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicationCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(medicationCategoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMedicationCategory() throws Exception {
        // Initialize the database
        insertedMedicationCategory = medicationCategoryRepository.saveAndFlush(medicationCategory);

        // Get the medicationCategory
        restMedicationCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, medicationCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicationCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMedicationCategory() throws Exception {
        // Get the medicationCategory
        restMedicationCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicationCategory() throws Exception {
        // Initialize the database
        insertedMedicationCategory = medicationCategoryRepository.saveAndFlush(medicationCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicationCategory
        MedicationCategory updatedMedicationCategory = medicationCategoryRepository.findById(medicationCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicationCategory are not directly saved in db
        em.detach(updatedMedicationCategory);
        updatedMedicationCategory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .color(UPDATED_COLOR)
            .icon(UPDATED_ICON)
            .createdDate(UPDATED_CREATED_DATE);
        MedicationCategoryDTO medicationCategoryDTO = medicationCategoryMapper.toDto(updatedMedicationCategory);

        restMedicationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicationCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicationCategoryToMatchAllProperties(updatedMedicationCategory);
    }

    @Test
    @Transactional
    void putNonExistingMedicationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationCategory.setId(longCount.incrementAndGet());

        // Create the MedicationCategory
        MedicationCategoryDTO medicationCategoryDTO = medicationCategoryMapper.toDto(medicationCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicationCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationCategory.setId(longCount.incrementAndGet());

        // Create the MedicationCategory
        MedicationCategoryDTO medicationCategoryDTO = medicationCategoryMapper.toDto(medicationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationCategory.setId(longCount.incrementAndGet());

        // Create the MedicationCategory
        MedicationCategoryDTO medicationCategoryDTO = medicationCategoryMapper.toDto(medicationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicationCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedMedicationCategory = medicationCategoryRepository.saveAndFlush(medicationCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicationCategory using partial update
        MedicationCategory partialUpdatedMedicationCategory = new MedicationCategory();
        partialUpdatedMedicationCategory.setId(medicationCategory.getId());

        partialUpdatedMedicationCategory.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE);

        restMedicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicationCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicationCategory))
            )
            .andExpect(status().isOk());

        // Validate the MedicationCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicationCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicationCategory, medicationCategory),
            getPersistedMedicationCategory(medicationCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicationCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedMedicationCategory = medicationCategoryRepository.saveAndFlush(medicationCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicationCategory using partial update
        MedicationCategory partialUpdatedMedicationCategory = new MedicationCategory();
        partialUpdatedMedicationCategory.setId(medicationCategory.getId());

        partialUpdatedMedicationCategory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .color(UPDATED_COLOR)
            .icon(UPDATED_ICON)
            .createdDate(UPDATED_CREATED_DATE);

        restMedicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicationCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicationCategory))
            )
            .andExpect(status().isOk());

        // Validate the MedicationCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicationCategoryUpdatableFieldsEquals(
            partialUpdatedMedicationCategory,
            getPersistedMedicationCategory(partialUpdatedMedicationCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMedicationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationCategory.setId(longCount.incrementAndGet());

        // Create the MedicationCategory
        MedicationCategoryDTO medicationCategoryDTO = medicationCategoryMapper.toDto(medicationCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicationCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationCategory.setId(longCount.incrementAndGet());

        // Create the MedicationCategory
        MedicationCategoryDTO medicationCategoryDTO = medicationCategoryMapper.toDto(medicationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationCategory.setId(longCount.incrementAndGet());

        // Create the MedicationCategory
        MedicationCategoryDTO medicationCategoryDTO = medicationCategoryMapper.toDto(medicationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicationCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicationCategory() throws Exception {
        // Initialize the database
        insertedMedicationCategory = medicationCategoryRepository.saveAndFlush(medicationCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicationCategory
        restMedicationCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicationCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicationCategoryRepository.count();
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

    protected MedicationCategory getPersistedMedicationCategory(MedicationCategory medicationCategory) {
        return medicationCategoryRepository.findById(medicationCategory.getId()).orElseThrow();
    }

    protected void assertPersistedMedicationCategoryToMatchAllProperties(MedicationCategory expectedMedicationCategory) {
        assertMedicationCategoryAllPropertiesEquals(expectedMedicationCategory, getPersistedMedicationCategory(expectedMedicationCategory));
    }

    protected void assertPersistedMedicationCategoryToMatchUpdatableProperties(MedicationCategory expectedMedicationCategory) {
        assertMedicationCategoryAllUpdatablePropertiesEquals(
            expectedMedicationCategory,
            getPersistedMedicationCategory(expectedMedicationCategory)
        );
    }
}
