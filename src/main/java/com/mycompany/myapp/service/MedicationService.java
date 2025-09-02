package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Medication;
import com.mycompany.myapp.repository.MedicationRepository;
import com.mycompany.myapp.service.dto.MedicationDTO;
import com.mycompany.myapp.service.mapper.MedicationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Medication}.
 */
@Service
@Transactional
public class MedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationService.class);

    private final MedicationRepository medicationRepository;

    private final MedicationMapper medicationMapper;

    public MedicationService(MedicationRepository medicationRepository, MedicationMapper medicationMapper) {
        this.medicationRepository = medicationRepository;
        this.medicationMapper = medicationMapper;
    }

    /**
     * Save a medication.
     *
     * @param medicationDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicationDTO save(MedicationDTO medicationDTO) {
        LOG.debug("Request to save Medication : {}", medicationDTO);
        Medication medication = medicationMapper.toEntity(medicationDTO);
        medication = medicationRepository.save(medication);
        return medicationMapper.toDto(medication);
    }

    /**
     * Update a medication.
     *
     * @param medicationDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicationDTO update(MedicationDTO medicationDTO) {
        LOG.debug("Request to update Medication : {}", medicationDTO);
        Medication medication = medicationMapper.toEntity(medicationDTO);
        medication = medicationRepository.save(medication);
        return medicationMapper.toDto(medication);
    }

    /**
     * Partially update a medication.
     *
     * @param medicationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicationDTO> partialUpdate(MedicationDTO medicationDTO) {
        LOG.debug("Request to partially update Medication : {}", medicationDTO);

        return medicationRepository
            .findById(medicationDTO.getId())
            .map(existingMedication -> {
                medicationMapper.partialUpdate(existingMedication, medicationDTO);

                return existingMedication;
            })
            .map(medicationRepository::save)
            .map(medicationMapper::toDto);
    }

    /**
     * Get all the medications with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MedicationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return medicationRepository.findAllWithEagerRelationships(pageable).map(medicationMapper::toDto);
    }

    /**
     * Get one medication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicationDTO> findOne(Long id) {
        LOG.debug("Request to get Medication : {}", id);
        return medicationRepository.findOneWithEagerRelationships(id).map(medicationMapper::toDto);
    }

    /**
     * Delete the medication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Medication : {}", id);
        medicationRepository.deleteById(id);
    }
}
