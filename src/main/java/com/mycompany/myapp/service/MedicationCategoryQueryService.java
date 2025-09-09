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

    @Transactional(readOnly = true)
    public List<MedicationCategoryDTO> findByCriteria(MedicationCategoryCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<MedicationCategory> specification = createSpecification(criteria);
        return medicationCategoryMapper.toDto(medicationCategoryRepository.findAll(specification));
    }

    @Transactional(readOnly = true)
    public long countByCriteria(MedicationCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MedicationCategory> specification = createSpecification(criteria);
        return medicationCategoryRepository.count(specification);
    }

    // FIXED: Proper null handling instead of using Specification.allOf()
    protected Specification<MedicationCategory> createSpecification(MedicationCategoryCriteria criteria) {
        Specification<MedicationCategory> specification = Specification.where(null);
        if (criteria != null) {
            if (Boolean.TRUE.equals(criteria.getDistinct())) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MedicationCategory_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MedicationCategory_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MedicationCategory_.description));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOwnerId(), root -> root.join(MedicationCategory_.owner, JoinType.LEFT).get(User_.id))
                );
            }
            if (criteria.getMedicationsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMedicationsId(), root ->
                        root.join(MedicationCategory_.medications, JoinType.LEFT).get(Medication_.id)
                    )
                );
            }
        }
        return specification;
    }
}
