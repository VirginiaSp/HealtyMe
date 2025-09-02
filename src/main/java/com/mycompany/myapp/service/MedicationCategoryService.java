package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.MedicationCategory;
import com.mycompany.myapp.repository.MedicationCategoryRepository;
import com.mycompany.myapp.service.dto.MedicationCategoryDTO;
import com.mycompany.myapp.service.mapper.MedicationCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.MedicationCategory}.
 */
@Service
@Transactional
public class MedicationCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationCategoryService.class);

    private final MedicationCategoryRepository medicationCategoryRepository;

    private final MedicationCategoryMapper medicationCategoryMapper;

    public MedicationCategoryService(
        MedicationCategoryRepository medicationCategoryRepository,
        MedicationCategoryMapper medicationCategoryMapper
    ) {
        this.medicationCategoryRepository = medicationCategoryRepository;
        this.medicationCategoryMapper = medicationCategoryMapper;
    }

    /**
     * Save a medicationCategory.
     *
     * @param medicationCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicationCategoryDTO save(MedicationCategoryDTO medicationCategoryDTO) {
        LOG.debug("Request to save MedicationCategory : {}", medicationCategoryDTO);
        MedicationCategory medicationCategory = medicationCategoryMapper.toEntity(medicationCategoryDTO);
        medicationCategory = medicationCategoryRepository.save(medicationCategory);
        return medicationCategoryMapper.toDto(medicationCategory);
    }

    /**
     * Update a medicationCategory.
     *
     * @param medicationCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicationCategoryDTO update(MedicationCategoryDTO medicationCategoryDTO) {
        LOG.debug("Request to update MedicationCategory : {}", medicationCategoryDTO);
        MedicationCategory medicationCategory = medicationCategoryMapper.toEntity(medicationCategoryDTO);
        medicationCategory = medicationCategoryRepository.save(medicationCategory);
        return medicationCategoryMapper.toDto(medicationCategory);
    }

    /**
     * Partially update a medicationCategory.
     *
     * @param medicationCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicationCategoryDTO> partialUpdate(MedicationCategoryDTO medicationCategoryDTO) {
        LOG.debug("Request to partially update MedicationCategory : {}", medicationCategoryDTO);

        return medicationCategoryRepository
            .findById(medicationCategoryDTO.getId())
            .map(existingMedicationCategory -> {
                medicationCategoryMapper.partialUpdate(existingMedicationCategory, medicationCategoryDTO);

                return existingMedicationCategory;
            })
            .map(medicationCategoryRepository::save)
            .map(medicationCategoryMapper::toDto);
    }

    /**
     * Get all the medicationCategories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MedicationCategoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return medicationCategoryRepository.findAllWithEagerRelationships(pageable).map(medicationCategoryMapper::toDto);
    }

    /**
     * Get one medicationCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicationCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get MedicationCategory : {}", id);
        return medicationCategoryRepository.findOneWithEagerRelationships(id).map(medicationCategoryMapper::toDto);
    }

    /**
     * Delete the medicationCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MedicationCategory : {}", id);
        medicationCategoryRepository.deleteById(id);
    }
}
