package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Medication} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private Integer rating;

    @Lob
    private String notes;

    @Size(max = 200)
    private String dosage;

    @Size(max = 100)
    private String frequency;

    @Size(max = 500)
    private String sideEffects;

    private LocalDate createdDate;

    private LocalDate lastTaken;

    private UserDTO owner;

    private Set<MedicationCategoryDTO> categories = new HashSet<>();

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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getLastTaken() {
        return lastTaken;
    }

    public void setLastTaken(LocalDate lastTaken) {
        this.lastTaken = lastTaken;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public Set<MedicationCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<MedicationCategoryDTO> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicationDTO)) {
            return false;
        }

        MedicationDTO medicationDTO = (MedicationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rating=" + getRating() +
            ", notes='" + getNotes() + "'" +
            ", dosage='" + getDosage() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", sideEffects='" + getSideEffects() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastTaken='" + getLastTaken() + "'" +
            ", owner=" + getOwner() +
            ", categories=" + getCategories() +
            "}";
    }
}
