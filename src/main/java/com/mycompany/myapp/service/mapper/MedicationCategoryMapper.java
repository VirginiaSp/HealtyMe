package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Medication;
import com.mycompany.myapp.domain.MedicationCategory;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.MedicationCategoryDTO;
import com.mycompany.myapp.service.dto.MedicationDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MedicationCategory} and its DTO {@link MedicationCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicationCategoryMapper extends EntityMapper<MedicationCategoryDTO, MedicationCategory> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userLogin")
    @Mapping(target = "medications", source = "medications", qualifiedByName = "medicationIdSet")
    MedicationCategoryDTO toDto(MedicationCategory s);

    @Mapping(target = "medications", ignore = true)
    @Mapping(target = "removeMedications", ignore = true)
    MedicationCategory toEntity(MedicationCategoryDTO medicationCategoryDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("medicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicationDTO toDtoMedicationId(Medication medication);

    @Named("medicationIdSet")
    default Set<MedicationDTO> toDtoMedicationIdSet(Set<Medication> medication) {
        return medication.stream().map(this::toDtoMedicationId).collect(Collectors.toSet());
    }
}
