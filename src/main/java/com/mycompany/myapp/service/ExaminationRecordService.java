package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ExaminationRecord;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.ExaminationRecordRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.ExaminationRecordDTO;
import com.mycompany.myapp.service.mapper.ExaminationRecordMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ExaminationRecord}.
 */
@Service
@Transactional
public class ExaminationRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(ExaminationRecordService.class);

    private final ExaminationRecordRepository examinationRecordRepository;
    private final ExaminationRecordMapper examinationRecordMapper;
    private final UserRepository userRepository; // ADD THIS

    public ExaminationRecordService(
        ExaminationRecordRepository examinationRecordRepository,
        ExaminationRecordMapper examinationRecordMapper,
        UserRepository userRepository // ADD THIS PARAMETER
    ) {
        this.examinationRecordRepository = examinationRecordRepository;
        this.examinationRecordMapper = examinationRecordMapper;
        this.userRepository = userRepository; // ADD THIS LINE
    }

    /**
     * Save a examinationRecord.
     *
     * @param examinationRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public ExaminationRecordDTO save(ExaminationRecordDTO examinationRecordDTO) {
        LOG.debug("Request to save ExaminationRecord : {}", examinationRecordDTO);
        ExaminationRecord examinationRecord = examinationRecordMapper.toEntity(examinationRecordDTO);
        examinationRecord = examinationRecordRepository.save(examinationRecord);
        return examinationRecordMapper.toDto(examinationRecord);
    }

    /**
     * Save a examinationRecord with current user as owner.
     *
     * @param examinationRecordDTO the entity to save.
     * @param currentUserLogin the login of the current user.
     * @return the persisted entity.
     */
    public ExaminationRecordDTO saveWithCurrentUser(ExaminationRecordDTO examinationRecordDTO, String currentUserLogin) {
        LOG.debug("Request to save ExaminationRecord with current user : {}", examinationRecordDTO);

        // Find current user
        User currentUser = userRepository
            .findOneByLogin(currentUserLogin)
            .orElseThrow(() -> new RuntimeException("User not found: " + currentUserLogin));

        // Convert DTO to entity
        ExaminationRecord examinationRecord = examinationRecordMapper.toEntity(examinationRecordDTO);

        // Set the owner
        examinationRecord.setOwner(currentUser);

        // Save and return
        examinationRecord = examinationRecordRepository.save(examinationRecord);
        return examinationRecordMapper.toDto(examinationRecord);
    }

    /**
     * Update a examinationRecord.
     *
     * @param examinationRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public ExaminationRecordDTO update(ExaminationRecordDTO examinationRecordDTO) {
        LOG.debug("Request to update ExaminationRecord : {}", examinationRecordDTO);
        ExaminationRecord examinationRecord = examinationRecordMapper.toEntity(examinationRecordDTO);
        examinationRecord = examinationRecordRepository.save(examinationRecord);
        return examinationRecordMapper.toDto(examinationRecord);
    }

    /**
     * Partially update a examinationRecord.
     *
     * @param examinationRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExaminationRecordDTO> partialUpdate(ExaminationRecordDTO examinationRecordDTO) {
        LOG.debug("Request to partially update ExaminationRecord : {}", examinationRecordDTO);

        return examinationRecordRepository
            .findById(examinationRecordDTO.getId())
            .map(existingExaminationRecord -> {
                examinationRecordMapper.partialUpdate(existingExaminationRecord, examinationRecordDTO);

                return existingExaminationRecord;
            })
            .map(examinationRecordRepository::save)
            .map(examinationRecordMapper::toDto);
    }

    /**
     * Get all the examinationRecords with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ExaminationRecordDTO> findAllWithEagerRelationships(Pageable pageable) {
        return examinationRecordRepository.findAllWithEagerRelationships(pageable).map(examinationRecordMapper::toDto);
    }

    /**
     * Get one examinationRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExaminationRecordDTO> findOne(Long id) {
        LOG.debug("Request to get ExaminationRecord : {}", id);
        return examinationRecordRepository.findOneWithEagerRelationships(id).map(examinationRecordMapper::toDto);
    }

    /**
     * Delete the examinationRecord by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExaminationRecord : {}", id);
        examinationRecordRepository.deleteById(id);
    }
}
