package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.MedicationPlanDose;
import com.mycompany.myapp.repository.MedicationPlanDoseRepository;
import com.mycompany.myapp.service.dto.MedicationPlanDoseDTO;
import com.mycompany.myapp.service.mapper.MedicationPlanDoseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.MedicationPlanDose}.
 */
@Service
@Transactional
public class MedicationPlanDoseService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationPlanDoseService.class);

    private final MedicationPlanDoseRepository medicationPlanDoseRepository;

    private final MedicationPlanDoseMapper medicationPlanDoseMapper;

    public MedicationPlanDoseService(
        MedicationPlanDoseRepository medicationPlanDoseRepository,
        MedicationPlanDoseMapper medicationPlanDoseMapper
    ) {
        this.medicationPlanDoseRepository = medicationPlanDoseRepository;
        this.medicationPlanDoseMapper = medicationPlanDoseMapper;
    }

    /**
     * Save a medicationPlanDose.
     *
     * @param medicationPlanDoseDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicationPlanDoseDTO save(MedicationPlanDoseDTO medicationPlanDoseDTO) {
        LOG.debug("Request to save MedicationPlanDose : {}", medicationPlanDoseDTO);
        MedicationPlanDose medicationPlanDose = medicationPlanDoseMapper.toEntity(medicationPlanDoseDTO);
        medicationPlanDose = medicationPlanDoseRepository.save(medicationPlanDose);
        return medicationPlanDoseMapper.toDto(medicationPlanDose);
    }

    /**
     * Update a medicationPlanDose.
     *
     * @param medicationPlanDoseDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicationPlanDoseDTO update(MedicationPlanDoseDTO medicationPlanDoseDTO) {
        LOG.debug("Request to update MedicationPlanDose : {}", medicationPlanDoseDTO);
        MedicationPlanDose medicationPlanDose = medicationPlanDoseMapper.toEntity(medicationPlanDoseDTO);
        medicationPlanDose = medicationPlanDoseRepository.save(medicationPlanDose);
        return medicationPlanDoseMapper.toDto(medicationPlanDose);
    }

    /**
     * Partially update a medicationPlanDose.
     *
     * @param medicationPlanDoseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicationPlanDoseDTO> partialUpdate(MedicationPlanDoseDTO medicationPlanDoseDTO) {
        LOG.debug("Request to partially update MedicationPlanDose : {}", medicationPlanDoseDTO);

        return medicationPlanDoseRepository
            .findById(medicationPlanDoseDTO.getId())
            .map(existingMedicationPlanDose -> {
                medicationPlanDoseMapper.partialUpdate(existingMedicationPlanDose, medicationPlanDoseDTO);

                return existingMedicationPlanDose;
            })
            .map(medicationPlanDoseRepository::save)
            .map(medicationPlanDoseMapper::toDto);
    }

    /**
     * Get all the medicationPlanDoses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MedicationPlanDoseDTO> findAllWithEagerRelationships(Pageable pageable) {
        return medicationPlanDoseRepository.findAllWithEagerRelationships(pageable).map(medicationPlanDoseMapper::toDto);
    }

    /**
     * Get one medicationPlanDose by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicationPlanDoseDTO> findOne(Long id) {
        LOG.debug("Request to get MedicationPlanDose : {}", id);
        return medicationPlanDoseRepository.findOneWithEagerRelationships(id).map(medicationPlanDoseMapper::toDto);
    }

    /**
     * Delete the medicationPlanDose by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MedicationPlanDose : {}", id);
        medicationPlanDoseRepository.deleteById(id);
    }
}
