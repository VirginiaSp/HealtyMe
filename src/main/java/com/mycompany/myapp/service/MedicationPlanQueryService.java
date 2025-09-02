package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.MedicationPlan;
import com.mycompany.myapp.repository.MedicationPlanRepository;
import com.mycompany.myapp.service.criteria.MedicationPlanCriteria;
import com.mycompany.myapp.service.dto.MedicationPlanDTO;
import com.mycompany.myapp.service.mapper.MedicationPlanMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MedicationPlan} entities in the database.
 * The main input is a {@link MedicationPlanCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MedicationPlanDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicationPlanQueryService extends QueryService<MedicationPlan> {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationPlanQueryService.class);

    private final MedicationPlanRepository medicationPlanRepository;

    private final MedicationPlanMapper medicationPlanMapper;

    public MedicationPlanQueryService(MedicationPlanRepository medicationPlanRepository, MedicationPlanMapper medicationPlanMapper) {
        this.medicationPlanRepository = medicationPlanRepository;
        this.medicationPlanMapper = medicationPlanMapper;
    }

    /**
     * Return a {@link List} of {@link MedicationPlanDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MedicationPlanDTO> findByCriteria(MedicationPlanCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<MedicationPlan> specification = createSpecification(criteria);
        return medicationPlanMapper.toDto(medicationPlanRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicationPlanCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MedicationPlan> specification = createSpecification(criteria);
        return medicationPlanRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicationPlanCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MedicationPlan> createSpecification(MedicationPlanCriteria criteria) {
        Specification<MedicationPlan> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MedicationPlan_.id),
                buildStringSpecification(criteria.getPlanName(), MedicationPlan_.planName),
                buildRangeSpecification(criteria.getStartDate(), MedicationPlan_.startDate),
                buildRangeSpecification(criteria.getEndDate(), MedicationPlan_.endDate),
                buildStringSpecification(criteria.getMedicationName(), MedicationPlan_.medicationName),
                buildStringSpecification(criteria.getColorHex(), MedicationPlan_.colorHex),
                buildSpecification(criteria.getDosesId(), root ->
                    root.join(MedicationPlan_.doses, JoinType.LEFT).get(MedicationPlanDose_.id)
                ),
                buildSpecification(criteria.getOwnerId(), root -> root.join(MedicationPlan_.owner, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
