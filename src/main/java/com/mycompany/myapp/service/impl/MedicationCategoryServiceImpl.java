package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.MedicationCategory;
import com.mycompany.myapp.repository.MedicationCategoryRepository;
import com.mycompany.myapp.service.MedicationCategoryService;
import com.mycompany.myapp.service.dto.MedicationCategoryDTO;
import com.mycompany.myapp.service.mapper.MedicationCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MedicationCategoryServiceImpl implements MedicationCategoryService {

    private static final Logger log = LoggerFactory.getLogger(MedicationCategoryServiceImpl.class);

    private final MedicationCategoryRepository medicationCategoryRepository;
    private final MedicationCategoryMapper medicationCategoryMapper;

    public MedicationCategoryServiceImpl(
        MedicationCategoryRepository medicationCategoryRepository,
        MedicationCategoryMapper medicationCategoryMapper
    ) {
        this.medicationCategoryRepository = medicationCategoryRepository;
        this.medicationCategoryMapper = medicationCategoryMapper;
    }

    @Override
    public MedicationCategoryDTO save(MedicationCategoryDTO medicationCategoryDTO) {
        log.debug("Request to save MedicationCategory : {}", medicationCategoryDTO);
        MedicationCategory medicationCategory = medicationCategoryMapper.toEntity(medicationCategoryDTO);
        medicationCategory = medicationCategoryRepository.save(medicationCategory);
        return medicationCategoryMapper.toDto(medicationCategory);
    }

    @Override
    public MedicationCategoryDTO update(MedicationCategoryDTO medicationCategoryDTO) {
        log.debug("Request to update MedicationCategory : {}", medicationCategoryDTO);
        MedicationCategory medicationCategory = medicationCategoryMapper.toEntity(medicationCategoryDTO);
        medicationCategory = medicationCategoryRepository.save(medicationCategory);
        return medicationCategoryMapper.toDto(medicationCategory);
    }

    @Override
    public Optional<MedicationCategoryDTO> partialUpdate(MedicationCategoryDTO medicationCategoryDTO) {
        log.debug("Request to partially update MedicationCategory : {}", medicationCategoryDTO);

        return medicationCategoryRepository
            .findById(medicationCategoryDTO.getId())
            .map(existingMedicationCategory -> {
                medicationCategoryMapper.partialUpdate(existingMedicationCategory, medicationCategoryDTO);
                return existingMedicationCategory;
            })
            .map(medicationCategoryRepository::save)
            .map(medicationCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicationCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MedicationCategories");
        return medicationCategoryRepository.findAll(pageable).map(medicationCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicationCategoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return medicationCategoryRepository.findAll(pageable).map(medicationCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicationCategoryDTO> findOne(Long id) {
        log.debug("Request to get MedicationCategory : {}", id);
        return medicationCategoryRepository.findById(id).map(medicationCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MedicationCategory : {}", id);
        medicationCategoryRepository.deleteById(id);
    }
}
