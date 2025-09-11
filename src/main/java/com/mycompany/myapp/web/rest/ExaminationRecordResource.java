package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ExaminationRecordRepository;
import com.mycompany.myapp.security.SecurityUtils;
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

    @PostMapping("")
    public ResponseEntity<ExaminationRecordDTO> createExaminationRecord(@Valid @RequestBody ExaminationRecordDTO examinationRecordDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ExaminationRecord : {}", examinationRecordDTO);
        if (examinationRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new examinationRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("Current user login not found", ENTITY_NAME, "usernotfound"));

        examinationRecordDTO = examinationRecordService.saveWithCurrentUser(examinationRecordDTO, currentUserLogin);

        return ResponseEntity.created(new URI("/api/examination-records/" + examinationRecordDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, examinationRecordDTO.getId().toString()))
            .body(examinationRecordDTO);
    }

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

    // ADD THE MISSING GET METHODS:

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

    @GetMapping("/count")
    public ResponseEntity<Long> countExaminationRecords(ExaminationRecordCriteria criteria) {
        LOG.debug("REST request to count ExaminationRecords by criteria: {}", criteria);
        return ResponseEntity.ok().body(examinationRecordQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExaminationRecordDTO> getExaminationRecord(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExaminationRecord : {}", id);
        Optional<ExaminationRecordDTO> examinationRecordDTO = examinationRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examinationRecordDTO);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExaminationRecord(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExaminationRecord : {}", id);
        examinationRecordService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
