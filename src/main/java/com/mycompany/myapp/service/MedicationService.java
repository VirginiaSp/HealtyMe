package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MedicationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Medication}.
 */
public interface MedicationService {
    /**
     * Save a medication.
     *
     * @param medicationDTO the entity to save.
     * @return the persisted entity.
     */
    MedicationDTO save(MedicationDTO medicationDTO);

    /**
     * Updates a medication.
     *
     * @param medicationDTO the entity to update.
     * @return the persisted entity.
     */
    MedicationDTO update(MedicationDTO medicationDTO);

    /**
     * Partially updates a medication.
     *
     * @param medicationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedicationDTO> partialUpdate(MedicationDTO medicationDTO);

    /**
     * Get all the medications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MedicationDTO> findAll(Pageable pageable);

    /**
     * Get all the medications with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MedicationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" medication.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedicationDTO> findOne(Long id);

    /**
     * Delete the "id" medication.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
