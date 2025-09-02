package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.MedicationPlan;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.MedicationPlanDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MedicationPlan} and its DTO {@link MedicationPlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicationPlanMapper extends EntityMapper<MedicationPlanDTO, MedicationPlan> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userLogin")
    MedicationPlanDTO toDto(MedicationPlan s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
