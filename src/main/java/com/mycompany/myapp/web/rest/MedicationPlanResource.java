package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MedicationPlanRepository;
import com.mycompany.myapp.service.MedicationPlanQueryService;
import com.mycompany.myapp.service.MedicationPlanService;
import com.mycompany.myapp.service.criteria.MedicationPlanCriteria;
import com.mycompany.myapp.service.dto.MedicationPlanDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MedicationPlan}.
 */
@RestController
@RequestMapping("/api/medication-plans")
public class MedicationPlanResource {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationPlanResource.class);

    private static final String ENTITY_NAME = "medicationPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicationPlanService medicationPlanService;

    private final MedicationPlanRepository medicationPlanRepository;

    private final MedicationPlanQueryService medicationPlanQueryService;

    public MedicationPlanResource(
        MedicationPlanService medicationPlanService,
        MedicationPlanRepository medicationPlanRepository,
        MedicationPlanQueryService medicationPlanQueryService
    ) {
        this.medicationPlanService = medicationPlanService;
        this.medicationPlanRepository = medicationPlanRepository;
        this.medicationPlanQueryService = medicationPlanQueryService;
    }

    /**
     * {@code POST  /medication-plans} : Create a new medicationPlan.
     *
     * @param medicationPlanDTO the medicationPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicationPlanDTO, or with status {@code 400 (Bad Request)} if the medicationPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicationPlanDTO> createMedicationPlan(@Valid @RequestBody MedicationPlanDTO medicationPlanDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MedicationPlan : {}", medicationPlanDTO);
        if (medicationPlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicationPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicationPlanDTO = medicationPlanService.save(medicationPlanDTO);
        return ResponseEntity.created(new URI("/api/medication-plans/" + medicationPlanDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, medicationPlanDTO.getId().toString()))
            .body(medicationPlanDTO);
    }

    /**
     * {@code PUT  /medication-plans/:id} : Updates an existing medicationPlan.
     *
     * @param id the id of the medicationPlanDTO to save.
     * @param medicationPlanDTO the medicationPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicationPlanDTO,
     * or with status {@code 400 (Bad Request)} if the medicationPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicationPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicationPlanDTO> updateMedicationPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicationPlanDTO medicationPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MedicationPlan : {}, {}", id, medicationPlanDTO);
        if (medicationPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicationPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicationPlanDTO = medicationPlanService.update(medicationPlanDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicationPlanDTO.getId().toString()))
            .body(medicationPlanDTO);
    }

    /**
     * {@code PATCH  /medication-plans/:id} : Partial updates given fields of an existing medicationPlan, field will ignore if it is null
     *
     * @param id the id of the medicationPlanDTO to save.
     * @param medicationPlanDTO the medicationPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicationPlanDTO,
     * or with status {@code 400 (Bad Request)} if the medicationPlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicationPlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicationPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicationPlanDTO> partialUpdateMedicationPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicationPlanDTO medicationPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MedicationPlan partially : {}, {}", id, medicationPlanDTO);
        if (medicationPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicationPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicationPlanDTO> result = medicationPlanService.partialUpdate(medicationPlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicationPlanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medication-plans} : get all the medicationPlans.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicationPlans in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicationPlanDTO>> getAllMedicationPlans(MedicationPlanCriteria criteria) {
        LOG.debug("REST request to get MedicationPlans by criteria: {}", criteria);

        List<MedicationPlanDTO> entityList = medicationPlanQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /medication-plans/count} : count all the medicationPlans.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMedicationPlans(MedicationPlanCriteria criteria) {
        LOG.debug("REST request to count MedicationPlans by criteria: {}", criteria);
        return ResponseEntity.ok().body(medicationPlanQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /medication-plans/:id} : get the "id" medicationPlan.
     *
     * @param id the id of the medicationPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicationPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicationPlanDTO> getMedicationPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MedicationPlan : {}", id);
        Optional<MedicationPlanDTO> medicationPlanDTO = medicationPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicationPlanDTO);
    }

    /**
     * {@code DELETE  /medication-plans/:id} : delete the "id" medicationPlan.
     *
     * @param id the id of the medicationPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicationPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MedicationPlan : {}", id);
        medicationPlanService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
