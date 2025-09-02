package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.MedicationPlanDose} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicationPlanDoseDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "[0-2]\\d:[0-5]\\d")
    private String timeOfDay;

    @Size(max = 500)
    private String notes;

    @NotNull
    private UserDTO owner;

    @NotNull
    private MedicationPlanDTO plan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public MedicationPlanDTO getPlan() {
        return plan;
    }

    public void setPlan(MedicationPlanDTO plan) {
        this.plan = plan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicationPlanDoseDTO)) {
            return false;
        }

        MedicationPlanDoseDTO medicationPlanDoseDTO = (MedicationPlanDoseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicationPlanDoseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicationPlanDoseDTO{" +
            "id=" + getId() +
            ", timeOfDay='" + getTimeOfDay() + "'" +
            ", notes='" + getNotes() + "'" +
            ", owner=" + getOwner() +
            ", plan=" + getPlan() +
            "}";
    }
}
