package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.MedicationCategory;
import com.mycompany.myapp.repository.MedicationCategoryRepository;
import com.mycompany.myapp.service.criteria.MedicationCategoryCriteria;
import com.mycompany.myapp.service.dto.MedicationCategoryDTO;
import com.mycompany.myapp.service.mapper.MedicationCategoryMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MedicationCategory} entities in the database.
 * The main input is a {@link MedicationCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MedicationCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicationCategoryQueryService extends QueryService<MedicationCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationCategoryQueryService.class);

    private final MedicationCategoryRepository medicationCategoryRepository;

    private final MedicationCategoryMapper medicationCategoryMapper;

    public MedicationCategoryQueryService(
        MedicationCategoryRepository medicationCategoryRepository,
        MedicationCategoryMapper medicationCategoryMapper
    ) {
        this.medicationCategoryRepository = medicationCategoryRepository;
        this.medicationCategoryMapper = medicationCategoryMapper;
    }

    /**
     * Return a {@link List} of {@link MedicationCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MedicationCategoryDTO> findByCriteria(MedicationCategoryCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<MedicationCategory> specification = createSpecification(criteria);
        return medicationCategoryMapper.toDto(medicationCategoryRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicationCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MedicationCategory> specification = createSpecification(criteria);
        return medicationCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicationCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MedicationCategory> createSpecification(MedicationCategoryCriteria criteria) {
        Specification<MedicationCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MedicationCategory_.id),
                buildStringSpecification(criteria.getName(), MedicationCategory_.name),
                buildStringSpecification(criteria.getDescription(), MedicationCategory_.description),
                buildSpecification(criteria.getOwnerId(), root -> root.join(MedicationCategory_.owner, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getMedicationsId(), root ->
                    root.join(MedicationCategory_.medications, JoinType.LEFT).get(Medication_.id)
                )
            );
        }
        return specification;
    }
}
