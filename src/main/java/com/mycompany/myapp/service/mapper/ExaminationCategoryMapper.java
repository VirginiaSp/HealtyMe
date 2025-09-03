package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ExaminationCategory;
import com.mycompany.myapp.service.dto.ExaminationCategoryDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ExaminationCategoryMapper extends EntityMapper<ExaminationCategoryDTO, ExaminationCategory> {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget ExaminationCategory entity, ExaminationCategoryDTO dto);
}
