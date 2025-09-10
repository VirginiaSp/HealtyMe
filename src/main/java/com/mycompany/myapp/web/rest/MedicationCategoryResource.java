package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MedicationCategoryRepository;
import com.mycompany.myapp.service.MedicationCategoryService;
import com.mycompany.myapp.service.dto.MedicationCategoryDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MedicationCategory}.
 */
@RestController
@RequestMapping("/api/medication-categories")
public class MedicationCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationCategoryResource.class);

    private static final String ENTITY_NAME = "medicationCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicationCategoryService medicationCategoryService;

    private final MedicationCategoryRepository medicationCategoryRepository;

    public MedicationCategoryResource(
        MedicationCategoryService medicationCategoryService,
        MedicationCategoryRepository medicationCategoryRepository
    ) {
        this.medicationCategoryService = medicationCategoryService;
        this.medicationCategoryRepository = medicationCategoryRepository;
    }

    /**
     * {@code POST  /medication-categories} : Create a new medicationCategory.
     *
     * @param medicationCategoryDTO the medicationCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicationCategoryDTO, or with status {@code 400 (Bad Request)} if the medicationCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicationCategoryDTO> createMedicationCategory(@Valid @RequestBody MedicationCategoryDTO medicationCategoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MedicationCategory : {}", medicationCategoryDTO);
        if (medicationCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicationCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicationCategoryDTO = medicationCategoryService.save(medicationCategoryDTO);
        return ResponseEntity.created(new URI("/api/medication-categories/" + medicationCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, medicationCategoryDTO.getId().toString()))
            .body(medicationCategoryDTO);
    }

    /**
     * {@code PUT  /medication-categories/:id} : Updates an existing medicationCategory.
     *
     * @param id the id of the medicationCategoryDTO to save.
     * @param medicationCategoryDTO the medicationCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicationCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the medicationCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicationCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicationCategoryDTO> updateMedicationCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicationCategoryDTO medicationCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MedicationCategory : {}, {}", id, medicationCategoryDTO);
        if (medicationCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicationCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicationCategoryDTO = medicationCategoryService.update(medicationCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicationCategoryDTO.getId().toString()))
            .body(medicationCategoryDTO);
    }

    /**
     * {@code PATCH  /medication-categories/:id} : Partial updates given fields of an existing medicationCategory, field will ignore if it is null
     *
     * @param id the id of the medicationCategoryDTO to save.
     * @param medicationCategoryDTO the medicationCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicationCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the medicationCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicationCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicationCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicationCategoryDTO> partialUpdateMedicationCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicationCategoryDTO medicationCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MedicationCategory partially : {}, {}", id, medicationCategoryDTO);
        if (medicationCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicationCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicationCategoryDTO> result = medicationCategoryService.partialUpdate(medicationCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicationCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medication-categories} : get all the medicationCategories.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicationCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicationCategoryDTO>> getAllMedicationCategories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of MedicationCategories");
        Page<MedicationCategoryDTO> page;
        if (eagerload) {
            page = medicationCategoryService.findAllWithEagerRelationships(pageable);
        } else {
            page = medicationCategoryService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medication-categories/:id} : get the "id" medicationCategory.
     *
     * @param id the id of the medicationCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicationCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicationCategoryDTO> getMedicationCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MedicationCategory : {}", id);
        Optional<MedicationCategoryDTO> medicationCategoryDTO = medicationCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicationCategoryDTO);
    }

    /**
     * {@code DELETE  /medication-categories/:id} : delete the "id" medicationCategory.
     *
     * @param id the id of the medicationCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicationCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MedicationCategory : {}", id);
        medicationCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
