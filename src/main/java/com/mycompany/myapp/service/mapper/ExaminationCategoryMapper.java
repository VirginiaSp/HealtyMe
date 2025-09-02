package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ExaminationCategory;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.ExaminationCategoryDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExaminationCategory} and its DTO {@link ExaminationCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExaminationCategoryMapper extends EntityMapper<ExaminationCategoryDTO, ExaminationCategory> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userLogin")
    ExaminationCategoryDTO toDto(ExaminationCategory s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
