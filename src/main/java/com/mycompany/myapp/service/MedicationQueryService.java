package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Medication;
import com.mycompany.myapp.repository.MedicationRepository;
import com.mycompany.myapp.service.criteria.MedicationCriteria;
import com.mycompany.myapp.service.dto.MedicationDTO;
import com.mycompany.myapp.service.mapper.MedicationMapper;
import jakarta.persistence.criteria.JoinType;
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
public class MedicationQueryService extends QueryService<Medication> {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationQueryService.class);

    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;

    public MedicationQueryService(MedicationRepository medicationRepository, MedicationMapper medicationMapper) {
        this.medicationRepository = medicationRepository;
        this.medicationMapper = medicationMapper;
    }

    @Transactional(readOnly = true)
    public Page<MedicationDTO> findByCriteria(MedicationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medication> specification = createSpecification(criteria);
        return medicationRepository.fetchBagRelationships(medicationRepository.findAll(specification, page)).map(medicationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(MedicationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Medication> specification = createSpecification(criteria);
        return medicationRepository.count(specification);
    }

    // FIXED: Proper null handling instead of using Specification.allOf()
    protected Specification<Medication> createSpecification(MedicationCriteria criteria) {
        Specification<Medication> specification = Specification.where(null);
        if (criteria != null) {
            if (Boolean.TRUE.equals(criteria.getDistinct())) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Medication_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Medication_.name));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Medication_.rating));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), Medication_.notes));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOwnerId(), root -> root.join(Medication_.owner, JoinType.LEFT).get(User_.id))
                );
            }
            if (criteria.getCategoriesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoriesId(), root ->
                        root.join(Medication_.categories, JoinType.LEFT).get(MedicationCategory_.id)
                    )
                );
            }
        }
        return specification;
    }
}
