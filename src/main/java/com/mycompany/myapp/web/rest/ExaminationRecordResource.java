package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ExaminationRecordRepository;
import com.mycompany.myapp.service.ExaminationRecordQueryService;
import com.mycompany.myapp.service.ExaminationRecordService;
import com.mycompany.myapp.service.criteria.ExaminationRecordCriteria;
import com.mycompany.myapp.service.dto.ExaminationRecordDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ExaminationRecord}.
 */
@RestController
@RequestMapping("/api/examination-records")
public class ExaminationRecordResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExaminationRecordResource.class);

    private static final String ENTITY_NAME = "examinationRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExaminationRecordService examinationRecordService;

    private final ExaminationRecordRepository examinationRecordRepository;

    private final ExaminationRecordQueryService examinationRecordQueryService;

    public ExaminationRecordResource(
        ExaminationRecordService examinationRecordService,
        ExaminationRecordRepository examinationRecordRepository,
        ExaminationRecordQueryService examinationRecordQueryService
    ) {
        this.examinationRecordService = examinationRecordService;
        this.examinationRecordRepository = examinationRecordRepository;
        this.examinationRecordQueryService = examinationRecordQueryService;
    }

    /**
     * {@code POST  /examination-records} : Create a new examinationRecord.
     *
     * @param examinationRecordDTO the examinationRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examinationRecordDTO, or with status {@code 400 (Bad Request)} if the examinationRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExaminationRecordDTO> createExaminationRecord(@Valid @RequestBody ExaminationRecordDTO examinationRecordDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ExaminationRecord : {}", examinationRecordDTO);
        if (examinationRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new examinationRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        examinationRecordDTO = examinationRecordService.save(examinationRecordDTO);
        return ResponseEntity.created(new URI("/api/examination-records/" + examinationRecordDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, examinationRecordDTO.getId().toString()))
            .body(examinationRecordDTO);
    }

    /**
     * {@code PUT  /examination-records/:id} : Updates an existing examinationRecord.
     *
     * @param id the id of the examinationRecordDTO to save.
     * @param examinationRecordDTO the examinationRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examinationRecordDTO,
     * or with status {@code 400 (Bad Request)} if the examinationRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examinationRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExaminationRecordDTO> updateExaminationRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExaminationRecordDTO examinationRecordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExaminationRecord : {}, {}", id, examinationRecordDTO);
        if (examinationRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examinationRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examinationRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        examinationRecordDTO = examinationRecordService.update(examinationRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examinationRecordDTO.getId().toString()))
            .body(examinationRecordDTO);
    }

    /**
     * {@code PATCH  /examination-records/:id} : Partial updates given fields of an existing examinationRecord, field will ignore if it is null
     *
     * @param id the id of the examinationRecordDTO to save.
     * @param examinationRecordDTO the examinationRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examinationRecordDTO,
     * or with status {@code 400 (Bad Request)} if the examinationRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the examinationRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the examinationRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExaminationRecordDTO> partialUpdateExaminationRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExaminationRecordDTO examinationRecordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExaminationRecord partially : {}, {}", id, examinationRecordDTO);
        if (examinationRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examinationRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examinationRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExaminationRecordDTO> result = examinationRecordService.partialUpdate(examinationRecordDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examinationRecordDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /examination-records} : get all the examinationRecords.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examinationRecords in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExaminationRecordDTO>> getAllExaminationRecords(
        ExaminationRecordCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ExaminationRecords by criteria: {}", criteria);

        Page<ExaminationRecordDTO> page = examinationRecordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /examination-records/count} : count all the examinationRecords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countExaminationRecords(ExaminationRecordCriteria criteria) {
        LOG.debug("REST request to count ExaminationRecords by criteria: {}", criteria);
        return ResponseEntity.ok().body(examinationRecordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /examination-records/:id} : get the "id" examinationRecord.
     *
     * @param id the id of the examinationRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examinationRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExaminationRecordDTO> getExaminationRecord(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExaminationRecord : {}", id);
        Optional<ExaminationRecordDTO> examinationRecordDTO = examinationRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examinationRecordDTO);
    }

    /**
     * {@code DELETE  /examination-records/:id} : delete the "id" examinationRecord.
     *
     * @param id the id of the examinationRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExaminationRecord(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExaminationRecord : {}", id);
        examinationRecordService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
