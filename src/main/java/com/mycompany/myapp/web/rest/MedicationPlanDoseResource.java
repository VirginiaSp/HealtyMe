package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MedicationPlanDoseRepository;
import com.mycompany.myapp.service.MedicationPlanDoseQueryService;
import com.mycompany.myapp.service.MedicationPlanDoseService;
import com.mycompany.myapp.service.criteria.MedicationPlanDoseCriteria;
import com.mycompany.myapp.service.dto.MedicationPlanDoseDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MedicationPlanDose}.
 */
@RestController
@RequestMapping("/api/medication-plan-doses")
public class MedicationPlanDoseResource {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationPlanDoseResource.class);

    private static final String ENTITY_NAME = "medicationPlanDose";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicationPlanDoseService medicationPlanDoseService;

    private final MedicationPlanDoseRepository medicationPlanDoseRepository;

    private final MedicationPlanDoseQueryService medicationPlanDoseQueryService;

    public MedicationPlanDoseResource(
        MedicationPlanDoseService medicationPlanDoseService,
        MedicationPlanDoseRepository medicationPlanDoseRepository,
        MedicationPlanDoseQueryService medicationPlanDoseQueryService
    ) {
        this.medicationPlanDoseService = medicationPlanDoseService;
        this.medicationPlanDoseRepository = medicationPlanDoseRepository;
        this.medicationPlanDoseQueryService = medicationPlanDoseQueryService;
    }

    /**
     * {@code POST  /medication-plan-doses} : Create a new medicationPlanDose.
     *
     * @param medicationPlanDoseDTO the medicationPlanDoseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicationPlanDoseDTO, or with status {@code 400 (Bad Request)} if the medicationPlanDose has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicationPlanDoseDTO> createMedicationPlanDose(@Valid @RequestBody MedicationPlanDoseDTO medicationPlanDoseDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MedicationPlanDose : {}", medicationPlanDoseDTO);
        if (medicationPlanDoseDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicationPlanDose cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicationPlanDoseDTO = medicationPlanDoseService.save(medicationPlanDoseDTO);
        return ResponseEntity.created(new URI("/api/medication-plan-doses/" + medicationPlanDoseDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, medicationPlanDoseDTO.getId().toString()))
            .body(medicationPlanDoseDTO);
    }

    /**
     * {@code PUT  /medication-plan-doses/:id} : Updates an existing medicationPlanDose.
     *
     * @param id the id of the medicationPlanDoseDTO to save.
     * @param medicationPlanDoseDTO the medicationPlanDoseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicationPlanDoseDTO,
     * or with status {@code 400 (Bad Request)} if the medicationPlanDoseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicationPlanDoseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicationPlanDoseDTO> updateMedicationPlanDose(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicationPlanDoseDTO medicationPlanDoseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MedicationPlanDose : {}, {}", id, medicationPlanDoseDTO);
        if (medicationPlanDoseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicationPlanDoseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationPlanDoseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicationPlanDoseDTO = medicationPlanDoseService.update(medicationPlanDoseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicationPlanDoseDTO.getId().toString()))
            .body(medicationPlanDoseDTO);
    }

    /**
     * {@code PATCH  /medication-plan-doses/:id} : Partial updates given fields of an existing medicationPlanDose, field will ignore if it is null
     *
     * @param id the id of the medicationPlanDoseDTO to save.
     * @param medicationPlanDoseDTO the medicationPlanDoseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicationPlanDoseDTO,
     * or with status {@code 400 (Bad Request)} if the medicationPlanDoseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicationPlanDoseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicationPlanDoseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicationPlanDoseDTO> partialUpdateMedicationPlanDose(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicationPlanDoseDTO medicationPlanDoseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MedicationPlanDose partially : {}, {}", id, medicationPlanDoseDTO);
        if (medicationPlanDoseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicationPlanDoseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationPlanDoseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicationPlanDoseDTO> result = medicationPlanDoseService.partialUpdate(medicationPlanDoseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicationPlanDoseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medication-plan-doses} : get all the medicationPlanDoses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicationPlanDoses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicationPlanDoseDTO>> getAllMedicationPlanDoses(MedicationPlanDoseCriteria criteria) {
        LOG.debug("REST request to get MedicationPlanDoses by criteria: {}", criteria);

        List<MedicationPlanDoseDTO> entityList = medicationPlanDoseQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /medication-plan-doses/count} : count all the medicationPlanDoses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMedicationPlanDoses(MedicationPlanDoseCriteria criteria) {
        LOG.debug("REST request to count MedicationPlanDoses by criteria: {}", criteria);
        return ResponseEntity.ok().body(medicationPlanDoseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /medication-plan-doses/:id} : get the "id" medicationPlanDose.
     *
     * @param id the id of the medicationPlanDoseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicationPlanDoseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicationPlanDoseDTO> getMedicationPlanDose(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MedicationPlanDose : {}", id);
        Optional<MedicationPlanDoseDTO> medicationPlanDoseDTO = medicationPlanDoseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicationPlanDoseDTO);
    }

    /**
     * {@code DELETE  /medication-plan-doses/:id} : delete the "id" medicationPlanDose.
     *
     * @param id the id of the medicationPlanDoseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicationPlanDose(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MedicationPlanDose : {}", id);
        medicationPlanDoseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
