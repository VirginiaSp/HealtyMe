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
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userLogin")
    @Mapping(target = "medications", source = "medications", qualifiedByName = "medicationNameSet")
    MedicationCategoryDTO toDto(MedicationCategory s);

    @Mapping(target = "medications", ignore = true)
    @Mapping(target = "removeMedications", ignore = true)
    MedicationCategory toEntity(MedicationCategoryDTO medicationCategoryDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("medicationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MedicationDTO toDtoMedicationName(Medication medication);

    @Named("medicationNameSet")
    default Set<MedicationDTO> toDtoMedicationNameSet(Set<Medication> medication) {
        return medication.stream().map(this::toDtoMedicationName).collect(Collectors.toSet());
    }
}
