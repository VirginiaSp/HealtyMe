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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class MedicationCategoryQueryService extends QueryService<MedicationCategory> {

    private static final Logger log = LoggerFactory.getLogger(MedicationCategoryQueryService.class);

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
        log.debug("find by criteria : {}", criteria);
        final Specification<MedicationCategory> specification = createSpecification(criteria);
        return medicationCategoryMapper.toDto(medicationCategoryRepository.findAll(specification));
    }

    @Transactional(readOnly = true)
    public long countByCriteria(MedicationCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MedicationCategory> specification = createSpecification(criteria);
        return medicationCategoryRepository.count(specification);
    }

    protected Specification<MedicationCategory> createSpecification(MedicationCategoryCriteria criteria) {
        Specification<MedicationCategory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MedicationCategory_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MedicationCategory_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MedicationCategory_.description));
            }
            if (criteria.getColor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColor(), MedicationCategory_.color));
            }
            if (criteria.getIcon() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIcon(), MedicationCategory_.icon));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), MedicationCategory_.createdDate));
            }
            // Fixed: Use createdBy instead of owner
            if (criteria.getCreatedById() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCreatedById(), root ->
                        root.join(MedicationCategory_.createdBy, JoinType.LEFT).get(User_.id)
                    )
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
