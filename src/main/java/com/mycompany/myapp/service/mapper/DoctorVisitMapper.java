package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.domain.DoctorVisit;
import com.mycompany.myapp.service.dto.DoctorVisitDTO;
import org.springframework.stereotype.Component;

@Component
public class DoctorVisitMapper {

    public DoctorVisitDTO toDto(DoctorVisit entity) {
        if (entity == null) return null;
        DoctorVisitDTO dto = new DoctorVisitDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setType(entity.getType());
        dto.setNotes(entity.getNotes());
        dto.setDoctorId(entity.getDoctor() != null ? entity.getDoctor().getId() : null);
        return dto;
    }

    public DoctorVisit toEntity(DoctorVisitDTO dto, Doctor doctor) {
        if (dto == null) return null;
        DoctorVisit entity = new DoctorVisit();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setType(dto.getType());
        entity.setNotes(dto.getNotes());
        entity.setDoctor(doctor);
        return entity;
    }
}
