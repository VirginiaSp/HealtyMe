package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ExaminationCategoryAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ExaminationCategory;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.ExaminationCategoryRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.ExaminationCategoryService;
import com.mycompany.myapp.service.dto.ExaminationCategoryDTO;
import com.mycompany.myapp.service.mapper.ExaminationCategoryMapper;
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
 * Integration tests for the {@link ExaminationCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExaminationCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/examination-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExaminationCategoryRepository examinationCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ExaminationCategoryRepository examinationCategoryRepositoryMock;

    @Autowired
    private ExaminationCategoryMapper examinationCategoryMapper;

    @Mock
    private ExaminationCategoryService examinationCategoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExaminationCategoryMockMvc;

    private ExaminationCategory examinationCategory;

    private ExaminationCategory insertedExaminationCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExaminationCategory createEntity(EntityManager em) {
        ExaminationCategory examinationCategory = new ExaminationCategory().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        examinationCategory.setOwner(user);
        return examinationCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExaminationCategory createUpdatedEntity(EntityManager em) {
        ExaminationCategory updatedExaminationCategory = new ExaminationCategory().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedExaminationCategory.setOwner(user);
        return updatedExaminationCategory;
    }

    @BeforeEach
    void initTest() {
        examinationCategory = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedExaminationCategory != null) {
            examinationCategoryRepository.delete(insertedExaminationCategory);
            insertedExaminationCategory = null;
        }
    }

    @Test
    @Transactional
    void createExaminationCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExaminationCategory
        ExaminationCategoryDTO examinationCategoryDTO = examinationCategoryMapper.toDto(examinationCategory);
        var returnedExaminationCategoryDTO = om.readValue(
            restExaminationCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExaminationCategoryDTO.class
        );

        // Validate the ExaminationCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExaminationCategory = examinationCategoryMapper.toEntity(returnedExaminationCategoryDTO);
        assertExaminationCategoryUpdatableFieldsEquals(
            returnedExaminationCategory,
            getPersistedExaminationCategory(returnedExaminationCategory)
        );

        insertedExaminationCategory = returnedExaminationCategory;
    }

    @Test
    @Transactional
    void createExaminationCategoryWithExistingId() throws Exception {
        // Create the ExaminationCategory with an existing ID
        examinationCategory.setId(1L);
        ExaminationCategoryDTO examinationCategoryDTO = examinationCategoryMapper.toDto(examinationCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExaminationCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExaminationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examinationCategory.setName(null);

        // Create the ExaminationCategory, which fails.
        ExaminationCategoryDTO examinationCategoryDTO = examinationCategoryMapper.toDto(examinationCategory);

        restExaminationCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExaminationCategories() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        // Get all the examinationCategoryList
        restExaminationCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examinationCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExaminationCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(examinationCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExaminationCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(examinationCategoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExaminationCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(examinationCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExaminationCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(examinationCategoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getExaminationCategory() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        // Get the examinationCategory
        restExaminationCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, examinationCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examinationCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getExaminationCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        Long id = examinationCategory.getId();

        defaultExaminationCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultExaminationCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultExaminationCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExaminationCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        // Get all the examinationCategoryList where name equals to
        defaultExaminationCategoryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExaminationCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        // Get all the examinationCategoryList where name in
        defaultExaminationCategoryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExaminationCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        // Get all the examinationCategoryList where name is not null
        defaultExaminationCategoryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllExaminationCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        // Get all the examinationCategoryList where name contains
        defaultExaminationCategoryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExaminationCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        // Get all the examinationCategoryList where name does not contain
        defaultExaminationCategoryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllExaminationCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        // Get all the examinationCategoryList where description equals to
        defaultExaminationCategoryFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExaminationCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        // Get all the examinationCategoryList where description in
        defaultExaminationCategoryFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllExaminationCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        // Get all the examinationCategoryList where description is not null
        defaultExaminationCategoryFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllExaminationCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        // Get all the examinationCategoryList where description contains
        defaultExaminationCategoryFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllExaminationCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        // Get all the examinationCategoryList where description does not contain
        defaultExaminationCategoryFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllExaminationCategoriesByOwnerIsEqualToSomething() throws Exception {
        User owner;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            examinationCategoryRepository.saveAndFlush(examinationCategory);
            owner = UserResourceIT.createEntity();
        } else {
            owner = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(owner);
        em.flush();
        examinationCategory.setOwner(owner);
        examinationCategoryRepository.saveAndFlush(examinationCategory);
        Long ownerId = owner.getId();
        // Get all the examinationCategoryList where owner equals to ownerId
        defaultExaminationCategoryShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the examinationCategoryList where owner equals to (ownerId + 1)
        defaultExaminationCategoryShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    private void defaultExaminationCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultExaminationCategoryShouldBeFound(shouldBeFound);
        defaultExaminationCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExaminationCategoryShouldBeFound(String filter) throws Exception {
        restExaminationCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examinationCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restExaminationCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExaminationCategoryShouldNotBeFound(String filter) throws Exception {
        restExaminationCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExaminationCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExaminationCategory() throws Exception {
        // Get the examinationCategory
        restExaminationCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExaminationCategory() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examinationCategory
        ExaminationCategory updatedExaminationCategory = examinationCategoryRepository.findById(examinationCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExaminationCategory are not directly saved in db
        em.detach(updatedExaminationCategory);
        updatedExaminationCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        ExaminationCategoryDTO examinationCategoryDTO = examinationCategoryMapper.toDto(updatedExaminationCategory);

        restExaminationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examinationCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examinationCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExaminationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExaminationCategoryToMatchAllProperties(updatedExaminationCategory);
    }

    @Test
    @Transactional
    void putNonExistingExaminationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationCategory.setId(longCount.incrementAndGet());

        // Create the ExaminationCategory
        ExaminationCategoryDTO examinationCategoryDTO = examinationCategoryMapper.toDto(examinationCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExaminationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examinationCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examinationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExaminationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationCategory.setId(longCount.incrementAndGet());

        // Create the ExaminationCategory
        ExaminationCategoryDTO examinationCategoryDTO = examinationCategoryMapper.toDto(examinationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examinationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExaminationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationCategory.setId(longCount.incrementAndGet());

        // Create the ExaminationCategory
        ExaminationCategoryDTO examinationCategoryDTO = examinationCategoryMapper.toDto(examinationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examinationCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExaminationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExaminationCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examinationCategory using partial update
        ExaminationCategory partialUpdatedExaminationCategory = new ExaminationCategory();
        partialUpdatedExaminationCategory.setId(examinationCategory.getId());

        partialUpdatedExaminationCategory.name(UPDATED_NAME);

        restExaminationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExaminationCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExaminationCategory))
            )
            .andExpect(status().isOk());

        // Validate the ExaminationCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExaminationCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExaminationCategory, examinationCategory),
            getPersistedExaminationCategory(examinationCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateExaminationCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examinationCategory using partial update
        ExaminationCategory partialUpdatedExaminationCategory = new ExaminationCategory();
        partialUpdatedExaminationCategory.setId(examinationCategory.getId());

        partialUpdatedExaminationCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restExaminationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExaminationCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExaminationCategory))
            )
            .andExpect(status().isOk());

        // Validate the ExaminationCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExaminationCategoryUpdatableFieldsEquals(
            partialUpdatedExaminationCategory,
            getPersistedExaminationCategory(partialUpdatedExaminationCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingExaminationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationCategory.setId(longCount.incrementAndGet());

        // Create the ExaminationCategory
        ExaminationCategoryDTO examinationCategoryDTO = examinationCategoryMapper.toDto(examinationCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExaminationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examinationCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(examinationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExaminationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationCategory.setId(longCount.incrementAndGet());

        // Create the ExaminationCategory
        ExaminationCategoryDTO examinationCategoryDTO = examinationCategoryMapper.toDto(examinationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(examinationCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExaminationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examinationCategory.setId(longCount.incrementAndGet());

        // Create the ExaminationCategory
        ExaminationCategoryDTO examinationCategoryDTO = examinationCategoryMapper.toDto(examinationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(examinationCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExaminationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExaminationCategory() throws Exception {
        // Initialize the database
        insertedExaminationCategory = examinationCategoryRepository.saveAndFlush(examinationCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the examinationCategory
        restExaminationCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, examinationCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return examinationCategoryRepository.count();
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

    protected ExaminationCategory getPersistedExaminationCategory(ExaminationCategory examinationCategory) {
        return examinationCategoryRepository.findById(examinationCategory.getId()).orElseThrow();
    }

    protected void assertPersistedExaminationCategoryToMatchAllProperties(ExaminationCategory expectedExaminationCategory) {
        assertExaminationCategoryAllPropertiesEquals(
            expectedExaminationCategory,
            getPersistedExaminationCategory(expectedExaminationCategory)
        );
    }

    protected void assertPersistedExaminationCategoryToMatchUpdatableProperties(ExaminationCategory expectedExaminationCategory) {
        assertExaminationCategoryAllUpdatablePropertiesEquals(
            expectedExaminationCategory,
            getPersistedExaminationCategory(expectedExaminationCategory)
        );
    }
}
