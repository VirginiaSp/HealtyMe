package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.MedicationPlan;
import com.mycompany.myapp.domain.MedicationPlanDose;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.MedicationPlanDTO;
import com.mycompany.myapp.service.dto.MedicationPlanDoseDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MedicationPlanDose} and its DTO {@link MedicationPlanDoseDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicationPlanDoseMapper extends EntityMapper<MedicationPlanDoseDTO, MedicationPlanDose> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userLogin")
    @Mapping(target = "plan", source = "plan", qualifiedByName = "medicationPlanId")
    MedicationPlanDoseDTO toDto(MedicationPlanDose s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("medicationPlanId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicationPlanDTO toDtoMedicationPlanId(MedicationPlan medicationPlan);
}
