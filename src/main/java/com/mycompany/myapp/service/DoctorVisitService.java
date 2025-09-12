package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.domain.DoctorVisit;
import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.repository.DoctorVisitRepository;
import com.mycompany.myapp.service.dto.DoctorVisitDTO;
import com.mycompany.myapp.service.mapper.DoctorVisitMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DoctorVisitService {

    private final DoctorVisitRepository doctorVisitRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorVisitMapper doctorVisitMapper;

    public DoctorVisitService(
        DoctorVisitRepository doctorVisitRepository,
        DoctorRepository doctorRepository,
        DoctorVisitMapper doctorVisitMapper
    ) {
        this.doctorVisitRepository = doctorVisitRepository;
        this.doctorRepository = doctorRepository;
        this.doctorVisitMapper = doctorVisitMapper;
    }

    public DoctorVisitDTO save(Long doctorId, DoctorVisitDTO visitDTO) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        DoctorVisit visit = doctorVisitMapper.toEntity(visitDTO, doctor);
        visit = doctorVisitRepository.save(visit);
        return doctorVisitMapper.toDto(visit);
    }

    public DoctorVisitDTO update(Long doctorId, Long visitId, DoctorVisitDTO visitDTO) {
        // Find the existing visit
        DoctorVisit existingVisit = doctorVisitRepository
            .findByIdAndDoctorId(visitId, doctorId)
            .orElseThrow(() -> new RuntimeException("Visit not found"));

        // Update the visit
        existingVisit.setDate(visitDTO.getDate());
        existingVisit.setType(visitDTO.getType());
        existingVisit.setNotes(visitDTO.getNotes());

        // Save and return
        DoctorVisit savedVisit = doctorVisitRepository.save(existingVisit);
        return doctorVisitMapper.toDto(savedVisit);
    }

    public void delete(Long doctorId, Long visitId) {
        // Find the visit to ensure it belongs to the correct doctor
        DoctorVisit visit = doctorVisitRepository
            .findByIdAndDoctorId(visitId, doctorId)
            .orElseThrow(() -> new RuntimeException("Visit not found"));

        // Delete the visit
        doctorVisitRepository.delete(visit);
    }

    @Transactional(readOnly = true)
    public List<DoctorVisitDTO> findAllByDoctor(Long doctorId) {
        return doctorVisitRepository.findByDoctorId(doctorId).stream().map(doctorVisitMapper::toDto).collect(Collectors.toList());
    }
}
