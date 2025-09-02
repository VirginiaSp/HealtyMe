package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.MedicationPlan;
import com.mycompany.myapp.repository.MedicationPlanRepository;
import com.mycompany.myapp.service.dto.MedicationPlanDTO;
import com.mycompany.myapp.service.mapper.MedicationPlanMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.MedicationPlan}.
 */
@Service
@Transactional
public class MedicationPlanService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationPlanService.class);

    private final MedicationPlanRepository medicationPlanRepository;

    private final MedicationPlanMapper medicationPlanMapper;

    public MedicationPlanService(MedicationPlanRepository medicationPlanRepository, MedicationPlanMapper medicationPlanMapper) {
        this.medicationPlanRepository = medicationPlanRepository;
        this.medicationPlanMapper = medicationPlanMapper;
    }

    /**
     * Save a medicationPlan.
     *
     * @param medicationPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicationPlanDTO save(MedicationPlanDTO medicationPlanDTO) {
        LOG.debug("Request to save MedicationPlan : {}", medicationPlanDTO);
        MedicationPlan medicationPlan = medicationPlanMapper.toEntity(medicationPlanDTO);
        medicationPlan = medicationPlanRepository.save(medicationPlan);
        return medicationPlanMapper.toDto(medicationPlan);
    }

    /**
     * Update a medicationPlan.
     *
     * @param medicationPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicationPlanDTO update(MedicationPlanDTO medicationPlanDTO) {
        LOG.debug("Request to update MedicationPlan : {}", medicationPlanDTO);
        MedicationPlan medicationPlan = medicationPlanMapper.toEntity(medicationPlanDTO);
        medicationPlan = medicationPlanRepository.save(medicationPlan);
        return medicationPlanMapper.toDto(medicationPlan);
    }

    /**
     * Partially update a medicationPlan.
     *
     * @param medicationPlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicationPlanDTO> partialUpdate(MedicationPlanDTO medicationPlanDTO) {
        LOG.debug("Request to partially update MedicationPlan : {}", medicationPlanDTO);

        return medicationPlanRepository
            .findById(medicationPlanDTO.getId())
            .map(existingMedicationPlan -> {
                medicationPlanMapper.partialUpdate(existingMedicationPlan, medicationPlanDTO);

                return existingMedicationPlan;
            })
            .map(medicationPlanRepository::save)
            .map(medicationPlanMapper::toDto);
    }

    /**
     * Get all the medicationPlans with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MedicationPlanDTO> findAllWithEagerRelationships(Pageable pageable) {
        return medicationPlanRepository.findAllWithEagerRelationships(pageable).map(medicationPlanMapper::toDto);
    }

    /**
     * Get one medicationPlan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicationPlanDTO> findOne(Long id) {
        LOG.debug("Request to get MedicationPlan : {}", id);
        return medicationPlanRepository.findOneWithEagerRelationships(id).map(medicationPlanMapper::toDto);
    }

    /**
     * Delete the medicationPlan by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MedicationPlan : {}", id);
        medicationPlanRepository.deleteById(id);
    }
}
