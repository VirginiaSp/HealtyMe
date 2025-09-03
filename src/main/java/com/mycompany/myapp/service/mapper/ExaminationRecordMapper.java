package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ExaminationRecord;
import com.mycompany.myapp.service.dto.ExaminationRecordDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ExaminationRecordMapper extends EntityMapper<ExaminationRecordDTO, ExaminationRecord> {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget ExaminationRecord entity, ExaminationRecordDTO dto);
}
