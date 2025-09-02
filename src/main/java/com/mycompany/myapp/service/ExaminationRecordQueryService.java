package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ExaminationRecord;
import com.mycompany.myapp.domain.ExaminationRecord_;
import com.mycompany.myapp.repository.ExaminationRecordRepository;
import com.mycompany.myapp.service.criteria.ExaminationRecordCriteria;
import com.mycompany.myapp.service.dto.ExaminationRecordDTO;
import com.mycompany.myapp.service.mapper.ExaminationRecordMapper;
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
public class ExaminationRecordQueryService extends QueryService<ExaminationRecord> {

    private final Logger log = LoggerFactory.getLogger(ExaminationRecordQueryService.class);

    private final ExaminationRecordRepository examinationRecordRepository;

    private final ExaminationRecordMapper examinationRecordMapper;

    public ExaminationRecordQueryService(
        ExaminationRecordRepository examinationRecordRepository,
        ExaminationRecordMapper examinationRecordMapper
    ) {
        this.examinationRecordRepository = examinationRecordRepository;
        this.examinationRecordMapper = examinationRecordMapper;
    }

    @Transactional(readOnly = true)
    public List<ExaminationRecordDTO> findByCriteria(ExaminationRecordCriteria criteria) {
        final Specification<ExaminationRecord> specification = createSpecification(criteria);
        return examinationRecordMapper.toDto(examinationRecordRepository.findAll(specification));
    }

    @Transactional(readOnly = true)
    public Page<ExaminationRecordDTO> findByCriteria(ExaminationRecordCriteria criteria, Pageable page) {
        final Specification<ExaminationRecord> specification = createSpecification(criteria);
        return examinationRecordRepository.findAll(specification, page).map(examinationRecordMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(ExaminationRecordCriteria criteria) {
        final Specification<ExaminationRecord> specification = createSpecification(criteria);
        return examinationRecordRepository.count(specification);
    }

    protected Specification<ExaminationRecord> createSpecification(ExaminationRecordCriteria criteria) {
        Specification<ExaminationRecord> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExaminationRecord_.id));
            }
            if (criteria.getExamDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExamDate(), ExaminationRecord_.examDate));
            }
        }
        return specification;
    }
}
