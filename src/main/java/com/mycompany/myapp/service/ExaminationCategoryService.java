package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ExaminationCategory;
import com.mycompany.myapp.repository.ExaminationCategoryRepository;
import com.mycompany.myapp.service.dto.ExaminationCategoryDTO;
import com.mycompany.myapp.service.mapper.ExaminationCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ExaminationCategory}.
 */
@Service
@Transactional
public class ExaminationCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ExaminationCategoryService.class);

    private final ExaminationCategoryRepository examinationCategoryRepository;

    private final ExaminationCategoryMapper examinationCategoryMapper;

    public ExaminationCategoryService(
        ExaminationCategoryRepository examinationCategoryRepository,
        ExaminationCategoryMapper examinationCategoryMapper
    ) {
        this.examinationCategoryRepository = examinationCategoryRepository;
        this.examinationCategoryMapper = examinationCategoryMapper;
    }

    /**
     * Save a examinationCategory.
     *
     * @param examinationCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ExaminationCategoryDTO save(ExaminationCategoryDTO examinationCategoryDTO) {
        LOG.debug("Request to save ExaminationCategory : {}", examinationCategoryDTO);
        ExaminationCategory examinationCategory = examinationCategoryMapper.toEntity(examinationCategoryDTO);
        examinationCategory = examinationCategoryRepository.save(examinationCategory);
        return examinationCategoryMapper.toDto(examinationCategory);
    }

    /**
     * Update a examinationCategory.
     *
     * @param examinationCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ExaminationCategoryDTO update(ExaminationCategoryDTO examinationCategoryDTO) {
        LOG.debug("Request to update ExaminationCategory : {}", examinationCategoryDTO);
        ExaminationCategory examinationCategory = examinationCategoryMapper.toEntity(examinationCategoryDTO);
        examinationCategory = examinationCategoryRepository.save(examinationCategory);
        return examinationCategoryMapper.toDto(examinationCategory);
    }

    /**
     * Partially update a examinationCategory.
     *
     * @param examinationCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExaminationCategoryDTO> partialUpdate(ExaminationCategoryDTO examinationCategoryDTO) {
        LOG.debug("Request to partially update ExaminationCategory : {}", examinationCategoryDTO);

        return examinationCategoryRepository
            .findById(examinationCategoryDTO.getId())
            .map(existingExaminationCategory -> {
                examinationCategoryMapper.partialUpdate(existingExaminationCategory, examinationCategoryDTO);

                return existingExaminationCategory;
            })
            .map(examinationCategoryRepository::save)
            .map(examinationCategoryMapper::toDto);
    }

    /**
     * Get all the examinationCategories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ExaminationCategoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return examinationCategoryRepository.findAllWithEagerRelationships(pageable).map(examinationCategoryMapper::toDto);
    }

    /**
     * Get one examinationCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExaminationCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get ExaminationCategory : {}", id);
        return examinationCategoryRepository.findOneWithEagerRelationships(id).map(examinationCategoryMapper::toDto);
    }

    /**
     * Delete the examinationCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExaminationCategory : {}", id);
        examinationCategoryRepository.deleteById(id);
    }
}
