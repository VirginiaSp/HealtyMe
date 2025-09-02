package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ExaminationCategory;
import com.mycompany.myapp.domain.ExaminationRecord;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.ExaminationCategoryDTO;
import com.mycompany.myapp.service.dto.ExaminationRecordDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExaminationRecord} and its DTO {@link ExaminationRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExaminationRecordMapper extends EntityMapper<ExaminationRecordDTO, ExaminationRecord> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userLogin")
    @Mapping(target = "category", source = "category", qualifiedByName = "examinationCategoryId")
    ExaminationRecordDTO toDto(ExaminationRecord s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("examinationCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExaminationCategoryDTO toDtoExaminationCategoryId(ExaminationCategory examinationCategory);
}
