package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ExaminationCategory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExaminationCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    private UserDTO owner;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExaminationCategoryDTO)) {
            return false;
        }

        ExaminationCategoryDTO examinationCategoryDTO = (ExaminationCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examinationCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExaminationCategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", owner=" + getOwner() +
            "}";
    }
}
