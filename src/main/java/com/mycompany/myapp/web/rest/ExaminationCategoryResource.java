package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ExaminationCategoryRepository;
import com.mycompany.myapp.service.ExaminationCategoryQueryService;
import com.mycompany.myapp.service.ExaminationCategoryService;
import com.mycompany.myapp.service.criteria.ExaminationCategoryCriteria;
import com.mycompany.myapp.service.dto.ExaminationCategoryDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ExaminationCategory}.
 */
@RestController
@RequestMapping("/api/examination-categories")
public class ExaminationCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExaminationCategoryResource.class);

    private static final String ENTITY_NAME = "examinationCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExaminationCategoryService examinationCategoryService;

    private final ExaminationCategoryRepository examinationCategoryRepository;

    private final ExaminationCategoryQueryService examinationCategoryQueryService;

    public ExaminationCategoryResource(
        ExaminationCategoryService examinationCategoryService,
        ExaminationCategoryRepository examinationCategoryRepository,
        ExaminationCategoryQueryService examinationCategoryQueryService
    ) {
        this.examinationCategoryService = examinationCategoryService;
        this.examinationCategoryRepository = examinationCategoryRepository;
        this.examinationCategoryQueryService = examinationCategoryQueryService;
    }

    /**
     * {@code POST  /examination-categories} : Create a new examinationCategory.
     *
     * @param examinationCategoryDTO the examinationCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examinationCategoryDTO, or with status {@code 400 (Bad Request)} if the examinationCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExaminationCategoryDTO> createExaminationCategory(
        @Valid @RequestBody ExaminationCategoryDTO examinationCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ExaminationCategory : {}", examinationCategoryDTO);
        if (examinationCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new examinationCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        examinationCategoryDTO = examinationCategoryService.save(examinationCategoryDTO);
        return ResponseEntity.created(new URI("/api/examination-categories/" + examinationCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, examinationCategoryDTO.getId().toString()))
            .body(examinationCategoryDTO);
    }

    /**
     * {@code PUT  /examination-categories/:id} : Updates an existing examinationCategory.
     *
     * @param id the id of the examinationCategoryDTO to save.
     * @param examinationCategoryDTO the examinationCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examinationCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the examinationCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examinationCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExaminationCategoryDTO> updateExaminationCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExaminationCategoryDTO examinationCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExaminationCategory : {}, {}", id, examinationCategoryDTO);
        if (examinationCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examinationCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examinationCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        examinationCategoryDTO = examinationCategoryService.update(examinationCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examinationCategoryDTO.getId().toString()))
            .body(examinationCategoryDTO);
    }

    /**
     * {@code PATCH  /examination-categories/:id} : Partial updates given fields of an existing examinationCategory, field will ignore if it is null
     *
     * @param id the id of the examinationCategoryDTO to save.
     * @param examinationCategoryDTO the examinationCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examinationCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the examinationCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the examinationCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the examinationCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExaminationCategoryDTO> partialUpdateExaminationCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExaminationCategoryDTO examinationCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExaminationCategory partially : {}, {}", id, examinationCategoryDTO);
        if (examinationCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examinationCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examinationCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExaminationCategoryDTO> result = examinationCategoryService.partialUpdate(examinationCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examinationCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /examination-categories} : get all the examinationCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examinationCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExaminationCategoryDTO>> getAllExaminationCategories(ExaminationCategoryCriteria criteria) {
        LOG.debug("REST request to get ExaminationCategories by criteria: {}", criteria);

        List<ExaminationCategoryDTO> entityList = examinationCategoryQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /examination-categories/count} : count all the examinationCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countExaminationCategories(ExaminationCategoryCriteria criteria) {
        LOG.debug("REST request to count ExaminationCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(examinationCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /examination-categories/:id} : get the "id" examinationCategory.
     *
     * @param id the id of the examinationCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examinationCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExaminationCategoryDTO> getExaminationCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExaminationCategory : {}", id);
        Optional<ExaminationCategoryDTO> examinationCategoryDTO = examinationCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examinationCategoryDTO);
    }

    /**
     * {@code DELETE  /examination-categories/:id} : delete the "id" examinationCategory.
     *
     * @param id the id of the examinationCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExaminationCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExaminationCategory : {}", id);
        examinationCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
