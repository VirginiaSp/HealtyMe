package com.mycompany.myapp.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.service.DoctorQueryService;
import com.mycompany.myapp.service.DoctorService;
import com.mycompany.myapp.service.criteria.DoctorCriteria;
import com.mycompany.myapp.service.dto.DoctorDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
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
import org.springframework.security.core.context.SecurityContextHolder;
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
     * @param request the HTTP request containing the doctorDTO JSON.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doctorDTO, or with status {@code 400 (Bad Request)} if the doctor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DoctorDTO> createDoctor(HttpServletRequest request) throws URISyntaxException {
        System.out.println("NEW METHOD SIGNATURE BEING USED - HttpServletRequest");
        try {
            // Manually parse the JSON to bypass automatic validation
            ObjectMapper mapper = new ObjectMapper();
            DoctorDTO doctorDTO = mapper.readValue(request.getInputStream(), DoctorDTO.class);

            LOG.debug("=== ENTERING createDoctor method ===");
            LOG.debug("Manually parsed doctorDTO: {}", doctorDTO);

            // Always set the current user as owner, regardless of what's sent from frontend
            UserDTO currentUser = getCurrentUserAsDTO();
            LOG.debug("Setting owner to: {}", currentUser);
            doctorDTO.setOwner(currentUser);
            LOG.debug("After setting owner: {}", doctorDTO.getOwner());

            if (doctorDTO.getId() != null) {
                throw new BadRequestAlertException("A new doctor cannot already have an ID", ENTITY_NAME, "idexists");
            }

            // Manual validation for required fields
            if (doctorDTO.getFirstName() == null || doctorDTO.getFirstName().trim().isEmpty()) {
                throw new BadRequestAlertException("First name is required", ENTITY_NAME, "firstnamerequired");
            }
            if (doctorDTO.getLastName() == null || doctorDTO.getLastName().trim().isEmpty()) {
                throw new BadRequestAlertException("Last name is required", ENTITY_NAME, "lastnamerequired");
            }
            if (doctorDTO.getSpecialty() == null || doctorDTO.getSpecialty().trim().isEmpty()) {
                throw new BadRequestAlertException("Specialty is required", ENTITY_NAME, "specialtyrequired");
            }

            LOG.debug("About to save doctorDTO: {}", doctorDTO);
            doctorDTO = doctorService.save(doctorDTO);
            LOG.debug("Successfully saved doctor with ID: {}", doctorDTO.getId());

            return ResponseEntity.created(new URI("/api/doctors/" + doctorDTO.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, doctorDTO.getId().toString()))
                .body(doctorDTO);
        } catch (BadRequestAlertException e) {
            // Re-throw our custom exceptions
            throw e;
        } catch (IOException e) {
            LOG.error("Error parsing JSON request", e);
            throw new BadRequestAlertException("Invalid JSON format", ENTITY_NAME, "invalidjson");
        } catch (Exception e) {
            LOG.error("Error creating doctor", e);
            throw new BadRequestAlertException("Failed to create doctor: " + e.getMessage(), ENTITY_NAME, "creationfailed");
        }
    }

    /**
     * Helper method to get current user as UserDTO
     */
    private UserDTO getCurrentUserAsDTO() {
        // Get current user login from security context
        String currentLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        // We need to create a UserDTO with the actual user ID from database
        // For now, let's create a minimal UserDTO that the mapper can handle
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(currentLogin);
        // Set a dummy ID - the service layer should handle the actual user lookup
        userDTO.setId(1L); // This is a temporary fix

        LOG.debug("Created UserDTO: {}", userDTO);

        return userDTO;
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
