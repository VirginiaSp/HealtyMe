package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.MedicationPlanDose;
import com.mycompany.myapp.service.dto.MedicationPlanDoseDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MedicationPlanDoseMapper extends EntityMapper<MedicationPlanDoseDTO, MedicationPlanDose> {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget MedicationPlanDose entity, MedicationPlanDoseDTO dto);
}
