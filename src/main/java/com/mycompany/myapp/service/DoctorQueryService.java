// FIXED DoctorQueryService.java
package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.service.criteria.DoctorCriteria;
import com.mycompany.myapp.service.dto.DoctorDTO;
import com.mycompany.myapp.service.mapper.DoctorMapper;
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
 * Service for executing complex queries for {@link Doctor} entities in the database.
 * The main input is a {@link DoctorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DoctorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DoctorQueryService extends QueryService<Doctor> {

    private static final Logger LOG = LoggerFactory.getLogger(DoctorQueryService.class);

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public DoctorQueryService(DoctorRepository doctorRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
    }

    /**
     * Return a {@link Page} of {@link DoctorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DoctorDTO> findByCriteria(DoctorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Doctor> specification = createSpecification(criteria);
        return doctorRepository.findAll(specification, page).map(doctorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DoctorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Doctor> specification = createSpecification(criteria);
        return doctorRepository.count(specification);
    }

    /**
     * Function to convert {@link DoctorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    // FIXED: Replaced Specification.allOf() with proper null checks
    protected Specification<Doctor> createSpecification(DoctorCriteria criteria) {
        Specification<Doctor> specification = Specification.where(null);
        if (criteria != null) {
            if (Boolean.TRUE.equals(criteria.getDistinct())) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Doctor_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Doctor_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Doctor_.lastName));
            }
            if (criteria.getSpecialty() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpecialty(), Doctor_.specialty));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Doctor_.phone));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Doctor_.address));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), Doctor_.notes));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOwnerId(), root -> root.join(Doctor_.owner, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
