package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Medication;
import com.mycompany.myapp.repository.MedicationRepository;
import com.mycompany.myapp.service.MedicationService;
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
public class MedicationServiceImpl implements MedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationServiceImpl.class);

    private final MedicationRepository medicationRepository;

    private final MedicationMapper medicationMapper;

    public MedicationServiceImpl(MedicationRepository medicationRepository, MedicationMapper medicationMapper) {
        this.medicationRepository = medicationRepository;
        this.medicationMapper = medicationMapper;
    }

    @Override
    public MedicationDTO save(MedicationDTO medicationDTO) {
        LOG.debug("Request to save Medication : {}", medicationDTO);
        Medication medication = medicationMapper.toEntity(medicationDTO);
        medication = medicationRepository.save(medication);
        return medicationMapper.toDto(medication);
    }

    @Override
    public MedicationDTO update(MedicationDTO medicationDTO) {
        LOG.debug("Request to update Medication : {}", medicationDTO);
        Medication medication = medicationMapper.toEntity(medicationDTO);
        medication = medicationRepository.save(medication);
        return medicationMapper.toDto(medication);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public Page<MedicationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Medications");
        return medicationRepository.findAll(pageable).map(medicationMapper::toDto);
    }

    public Page<MedicationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return medicationRepository.findAllWithEagerRelationships(pageable).map(medicationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicationDTO> findOne(Long id) {
        LOG.debug("Request to get Medication : {}", id);
        return medicationRepository.findOneWithEagerRelationships(id).map(medicationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Medication : {}", id);
        medicationRepository.deleteById(id);
    }
}
