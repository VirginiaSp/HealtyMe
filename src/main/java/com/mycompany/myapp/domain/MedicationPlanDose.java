package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MedicationPlanDose.
 */
@Entity
@Table(name = "medication_plan_dose")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicationPlanDose implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Pattern(regexp = "[0-2]\\d:[0-5]\\d")
    @Column(name = "time_of_day", nullable = false)
    private String timeOfDay;

    @Size(max = 500)
    @Column(name = "notes", length = 500)
    private String notes;

    @ManyToOne(optional = false)
    @NotNull
    private User owner;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "doses", "owner" }, allowSetters = true)
    private MedicationPlan plan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MedicationPlanDose id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimeOfDay() {
        return this.timeOfDay;
    }

    public MedicationPlanDose timeOfDay(String timeOfDay) {
        this.setTimeOfDay(timeOfDay);
        return this;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getNotes() {
        return this.notes;
    }

    public MedicationPlanDose notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public MedicationPlanDose owner(User user) {
        this.setOwner(user);
        return this;
    }

    public MedicationPlan getPlan() {
        return this.plan;
    }

    public void setPlan(MedicationPlan medicationPlan) {
        this.plan = medicationPlan;
    }

    public MedicationPlanDose plan(MedicationPlan medicationPlan) {
        this.setPlan(medicationPlan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicationPlanDose)) {
            return false;
        }
        return getId() != null && getId().equals(((MedicationPlanDose) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicationPlanDose{" +
            "id=" + getId() +
            ", timeOfDay='" + getTimeOfDay() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
