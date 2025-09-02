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

/**
 * Service for executing complex queries for {@link Medication} entities in the database.
 * The main input is a {@link MedicationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MedicationDTO} which fulfills the criteria.
 */
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

    /**
     * Return a {@link Page} of {@link MedicationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicationDTO> findByCriteria(MedicationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medication> specification = createSpecification(criteria);
        return medicationRepository.fetchBagRelationships(medicationRepository.findAll(specification, page)).map(medicationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Medication> specification = createSpecification(criteria);
        return medicationRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Medication> createSpecification(MedicationCriteria criteria) {
        Specification<Medication> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Medication_.id),
                buildStringSpecification(criteria.getName(), Medication_.name),
                buildRangeSpecification(criteria.getRating(), Medication_.rating),
                buildStringSpecification(criteria.getNotes(), Medication_.notes),
                buildSpecification(criteria.getOwnerId(), root -> root.join(Medication_.owner, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getCategoriesId(), root ->
                    root.join(Medication_.categories, JoinType.LEFT).get(MedicationCategory_.id)
                )
            );
        }
        return specification;
    }
}
