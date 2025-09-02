package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ExaminationCategory;
import com.mycompany.myapp.repository.ExaminationCategoryRepository;
import com.mycompany.myapp.service.criteria.ExaminationCategoryCriteria;
import com.mycompany.myapp.service.dto.ExaminationCategoryDTO;
import com.mycompany.myapp.service.mapper.ExaminationCategoryMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ExaminationCategory} entities in the database.
 * The main input is a {@link ExaminationCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExaminationCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExaminationCategoryQueryService extends QueryService<ExaminationCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(ExaminationCategoryQueryService.class);

    private final ExaminationCategoryRepository examinationCategoryRepository;

    private final ExaminationCategoryMapper examinationCategoryMapper;

    public ExaminationCategoryQueryService(
        ExaminationCategoryRepository examinationCategoryRepository,
        ExaminationCategoryMapper examinationCategoryMapper
    ) {
        this.examinationCategoryRepository = examinationCategoryRepository;
        this.examinationCategoryMapper = examinationCategoryMapper;
    }

    /**
     * Return a {@link List} of {@link ExaminationCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExaminationCategoryDTO> findByCriteria(ExaminationCategoryCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ExaminationCategory> specification = createSpecification(criteria);
        return examinationCategoryMapper.toDto(examinationCategoryRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExaminationCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ExaminationCategory> specification = createSpecification(criteria);
        return examinationCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link ExaminationCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExaminationCategory> createSpecification(ExaminationCategoryCriteria criteria) {
        Specification<ExaminationCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ExaminationCategory_.id),
                buildStringSpecification(criteria.getName(), ExaminationCategory_.name),
                buildStringSpecification(criteria.getDescription(), ExaminationCategory_.description),
                buildSpecification(criteria.getOwnerId(), root -> root.join(ExaminationCategory_.owner, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
