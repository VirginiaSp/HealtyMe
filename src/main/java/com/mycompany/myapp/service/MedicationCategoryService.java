package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MedicationCategoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.MedicationCategory}.
 */
public interface MedicationCategoryService {
    /**
     * Save a medicationCategory.
     *
     * @param medicationCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    MedicationCategoryDTO save(MedicationCategoryDTO medicationCategoryDTO);

    /**
     * Updates a medicationCategory.
     *
     * @param medicationCategoryDTO the entity to update.
     * @return the persisted entity.
     */
    MedicationCategoryDTO update(MedicationCategoryDTO medicationCategoryDTO);

    /**
     * Partially updates a medicationCategory.
     *
     * @param medicationCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedicationCategoryDTO> partialUpdate(MedicationCategoryDTO medicationCategoryDTO);

    /**
     * Get all the medicationCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MedicationCategoryDTO> findAll(Pageable pageable);

    /**
     * Get all the medicationCategories with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MedicationCategoryDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" medicationCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedicationCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" medicationCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
