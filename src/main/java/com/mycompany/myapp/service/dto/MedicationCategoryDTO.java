package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.MedicationCategory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicationCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    private UserDTO owner;

    private Set<MedicationDTO> medications = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public Set<MedicationDTO> getMedications() {
        return medications;
    }

    public void setMedications(Set<MedicationDTO> medications) {
        this.medications = medications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicationCategoryDTO)) {
            return false;
        }

        MedicationCategoryDTO medicationCategoryDTO = (MedicationCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicationCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicationCategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", owner=" + getOwner() +
            ", medications=" + getMedications() +
            "}";
    }
}
