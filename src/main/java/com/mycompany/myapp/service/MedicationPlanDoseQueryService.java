package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.MedicationPlanDose;
import com.mycompany.myapp.repository.MedicationPlanDoseRepository;
import com.mycompany.myapp.service.criteria.MedicationPlanDoseCriteria;
import com.mycompany.myapp.service.dto.MedicationPlanDoseDTO;
import com.mycompany.myapp.service.mapper.MedicationPlanDoseMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MedicationPlanDose} entities in the database.
 * The main input is a {@link MedicationPlanDoseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MedicationPlanDoseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicationPlanDoseQueryService extends QueryService<MedicationPlanDose> {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationPlanDoseQueryService.class);

    private final MedicationPlanDoseRepository medicationPlanDoseRepository;

    private final MedicationPlanDoseMapper medicationPlanDoseMapper;

    public MedicationPlanDoseQueryService(
        MedicationPlanDoseRepository medicationPlanDoseRepository,
        MedicationPlanDoseMapper medicationPlanDoseMapper
    ) {
        this.medicationPlanDoseRepository = medicationPlanDoseRepository;
        this.medicationPlanDoseMapper = medicationPlanDoseMapper;
    }

    /**
     * Return a {@link List} of {@link MedicationPlanDoseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MedicationPlanDoseDTO> findByCriteria(MedicationPlanDoseCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<MedicationPlanDose> specification = createSpecification(criteria);
        return medicationPlanDoseMapper.toDto(medicationPlanDoseRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicationPlanDoseCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MedicationPlanDose> specification = createSpecification(criteria);
        return medicationPlanDoseRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicationPlanDoseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MedicationPlanDose> createSpecification(MedicationPlanDoseCriteria criteria) {
        Specification<MedicationPlanDose> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MedicationPlanDose_.id),
                buildStringSpecification(criteria.getTimeOfDay(), MedicationPlanDose_.timeOfDay),
                buildStringSpecification(criteria.getNotes(), MedicationPlanDose_.notes),
                buildSpecification(criteria.getOwnerId(), root -> root.join(MedicationPlanDose_.owner, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getPlanId(), root -> root.join(MedicationPlanDose_.plan, JoinType.LEFT).get(MedicationPlan_.id))
            );
        }
        return specification;
    }
}
