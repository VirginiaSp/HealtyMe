package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ExaminationRecord;
import com.mycompany.myapp.repository.ExaminationRecordRepository;
import com.mycompany.myapp.service.criteria.ExaminationRecordCriteria;
import com.mycompany.myapp.service.dto.ExaminationRecordDTO;
import com.mycompany.myapp.service.mapper.ExaminationRecordMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ExaminationRecord} entities in the database.
 * The main input is a {@link ExaminationRecordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ExaminationRecordDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExaminationRecordQueryService extends QueryService<ExaminationRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(ExaminationRecordQueryService.class);

    private final ExaminationRecordRepository examinationRecordRepository;

    private final ExaminationRecordMapper examinationRecordMapper;

    public ExaminationRecordQueryService(
        ExaminationRecordRepository examinationRecordRepository,
        ExaminationRecordMapper examinationRecordMapper
    ) {
        this.examinationRecordRepository = examinationRecordRepository;
        this.examinationRecordMapper = examinationRecordMapper;
    }

    /**
     * Return a {@link Page} of {@link ExaminationRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExaminationRecordDTO> findByCriteria(ExaminationRecordCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExaminationRecord> specification = createSpecification(criteria);
        return examinationRecordRepository.findAll(specification, page).map(examinationRecordMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExaminationRecordCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ExaminationRecord> specification = createSpecification(criteria);
        return examinationRecordRepository.count(specification);
    }

    /**
     * Function to convert {@link ExaminationRecordCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExaminationRecord> createSpecification(ExaminationRecordCriteria criteria) {
        Specification<ExaminationRecord> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ExaminationRecord_.id),
                buildStringSpecification(criteria.getTitle(), ExaminationRecord_.title),
                buildRangeSpecification(criteria.getExamDate(), ExaminationRecord_.examDate),
                buildStringSpecification(criteria.getOriginalFilename(), ExaminationRecord_.originalFilename),
                buildStringSpecification(criteria.getStoredFilename(), ExaminationRecord_.storedFilename),
                buildStringSpecification(criteria.getNotes(), ExaminationRecord_.notes),
                buildSpecification(criteria.getOwnerId(), root -> root.join(ExaminationRecord_.owner, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getCategoryId(), root ->
                    root.join(ExaminationRecord_.category, JoinType.LEFT).get(ExaminationCategory_.id)
                )
            );
        }
        return specification;
    }
}
