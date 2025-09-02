package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.service.DoctorQueryService;
import com.mycompany.myapp.service.DoctorService;
import com.mycompany.myapp.service.criteria.DoctorCriteria;
import com.mycompany.myapp.service.dto.DoctorDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Doctor}.
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorResource {

    private static final Logger LOG = LoggerFactory.getLogger(DoctorResource.class);

    private static final String ENTITY_NAME = "doctor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoctorService doctorService;

    private final DoctorRepository doctorRepository;

    private final DoctorQueryService doctorQueryService;

    public DoctorResource(DoctorService doctorService, DoctorRepository doctorRepository, DoctorQueryService doctorQueryService) {
        this.doctorService = doctorService;
        this.doctorRepository = doctorRepository;
        this.doctorQueryService = doctorQueryService;
    }

    /**
     * {@code POST  /doctors} : Create a new doctor.
     *
     * @param doctorDTO the doctorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doctorDTO, or with status {@code 400 (Bad Request)} if the doctor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Doctor : {}", doctorDTO);
        if (doctorDTO.getId() != null) {
            throw new BadRequestAlertException("A new doctor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        doctorDTO = doctorService.save(doctorDTO);
        return ResponseEntity.created(new URI("/api/doctors/" + doctorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, doctorDTO.getId().toString()))
            .body(doctorDTO);
    }

    /**
     * {@code PUT  /doctors/:id} : Updates an existing doctor.
     *
     * @param id the id of the doctorDTO to save.
     * @param doctorDTO the doctorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doctorDTO,
     * or with status {@code 400 (Bad Request)} if the doctorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doctorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DoctorDTO doctorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Doctor : {}, {}", id, doctorDTO);
        if (doctorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doctorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doctorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        doctorDTO = doctorService.update(doctorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doctorDTO.getId().toString()))
            .body(doctorDTO);
    }

    /**
     * {@code PATCH  /doctors/:id} : Partial updates given fields of an existing doctor, field will ignore if it is null
     *
     * @param id the id of the doctorDTO to save.
     * @param doctorDTO the doctorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doctorDTO,
     * or with status {@code 400 (Bad Request)} if the doctorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the doctorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the doctorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DoctorDTO> partialUpdateDoctor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DoctorDTO doctorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Doctor partially : {}, {}", id, doctorDTO);
        if (doctorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doctorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doctorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DoctorDTO> result = doctorService.partialUpdate(doctorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doctorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /doctors} : get all the doctors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doctors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors(
        DoctorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Doctors by criteria: {}", criteria);

        Page<DoctorDTO> page = doctorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doctors/count} : count all the doctors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDoctors(DoctorCriteria criteria) {
        LOG.debug("REST request to count Doctors by criteria: {}", criteria);
        return ResponseEntity.ok().body(doctorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /doctors/:id} : get the "id" doctor.
     *
     * @param id the id of the doctorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doctorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Doctor : {}", id);
        Optional<DoctorDTO> doctorDTO = doctorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorDTO);
    }

    /**
     * {@code DELETE  /doctors/:id} : delete the "id" doctor.
     *
     * @param id the id of the doctorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Doctor : {}", id);
        doctorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
