package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.DoctorVisitService;
import com.mycompany.myapp.service.dto.DoctorVisitDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors/{doctorId}/visits")
public class DoctorVisitResource {

    private final DoctorVisitService doctorVisitService;

    public DoctorVisitResource(DoctorVisitService doctorVisitService) {
        this.doctorVisitService = doctorVisitService;
    }

    @PostMapping("")
    public ResponseEntity<DoctorVisitDTO> createVisit(@PathVariable Long doctorId, @RequestBody DoctorVisitDTO visitDTO)
        throws URISyntaxException {
        DoctorVisitDTO result = doctorVisitService.save(doctorId, visitDTO);
        return ResponseEntity.created(new URI("/api/doctors/" + doctorId + "/visits/" + result.getId())).body(result);
    }

    @PutMapping("/{visitId}")
    public ResponseEntity<DoctorVisitDTO> updateVisit(
        @PathVariable Long doctorId,
        @PathVariable Long visitId,
        @RequestBody DoctorVisitDTO visitDTO
    ) {
        DoctorVisitDTO result = doctorVisitService.update(doctorId, visitId, visitDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{visitId}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long doctorId, @PathVariable Long visitId) {
        doctorVisitService.delete(doctorId, visitId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    public ResponseEntity<List<DoctorVisitDTO>> getVisits(@PathVariable Long doctorId) {
        List<DoctorVisitDTO> visits = doctorVisitService.findAllByDoctor(doctorId);
        return ResponseEntity.ok(visits);
    }
}
