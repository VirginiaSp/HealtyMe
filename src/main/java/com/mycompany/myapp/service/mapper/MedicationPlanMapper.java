package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.MedicationPlan;
import com.mycompany.myapp.service.dto.MedicationPlanDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MedicationPlanMapper extends EntityMapper<MedicationPlanDTO, MedicationPlan> {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget MedicationPlan entity, MedicationPlanDTO dto);
}
