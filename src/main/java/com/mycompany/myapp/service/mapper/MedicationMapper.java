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
 * Mapper for the entity {@link Medication} and its DTO {@link MedicationDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicationMapper extends EntityMapper<MedicationDTO, Medication> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userLogin")
    @Mapping(target = "categories", source = "categories", qualifiedByName = "medicationCategoryNameSet")
    MedicationDTO toDto(Medication s);

    @Mapping(target = "removeCategories", ignore = true)
    Medication toEntity(MedicationDTO medicationDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("medicationCategoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MedicationCategoryDTO toDtoMedicationCategoryName(MedicationCategory medicationCategory);

    @Named("medicationCategoryNameSet")
    default Set<MedicationCategoryDTO> toDtoMedicationCategoryNameSet(Set<MedicationCategory> medicationCategory) {
        return medicationCategory.stream().map(this::toDtoMedicationCategoryName).collect(Collectors.toSet());
    }
}
