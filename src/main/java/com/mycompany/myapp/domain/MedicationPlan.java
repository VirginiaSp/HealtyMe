package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MedicationPlan.
 */
@Entity
@Table(name = "medication_plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicationPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "plan_name", nullable = false)
    private String planName;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    @Column(name = "color_hex")
    private String colorHex;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "plan")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "owner", "plan" }, allowSetters = true)
    private Set<MedicationPlanDose> doses = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MedicationPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanName() {
        return this.planName;
    }

    public MedicationPlan planName(String planName) {
        this.setPlanName(planName);
        return this;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public MedicationPlan startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public MedicationPlan endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getMedicationName() {
        return this.medicationName;
    }

    public MedicationPlan medicationName(String medicationName) {
        this.setMedicationName(medicationName);
        return this;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getColorHex() {
        return this.colorHex;
    }

    public MedicationPlan colorHex(String colorHex) {
        this.setColorHex(colorHex);
        return this;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public Set<MedicationPlanDose> getDoses() {
        return this.doses;
    }

    public void setDoses(Set<MedicationPlanDose> medicationPlanDoses) {
        if (this.doses != null) {
            this.doses.forEach(i -> i.setPlan(null));
        }
        if (medicationPlanDoses != null) {
            medicationPlanDoses.forEach(i -> i.setPlan(this));
        }
        this.doses = medicationPlanDoses;
    }

    public MedicationPlan doses(Set<MedicationPlanDose> medicationPlanDoses) {
        this.setDoses(medicationPlanDoses);
        return this;
    }

    public MedicationPlan addDoses(MedicationPlanDose medicationPlanDose) {
        this.doses.add(medicationPlanDose);
        medicationPlanDose.setPlan(this);
        return this;
    }

    public MedicationPlan removeDoses(MedicationPlanDose medicationPlanDose) {
        this.doses.remove(medicationPlanDose);
        medicationPlanDose.setPlan(null);
        return this;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public MedicationPlan owner(User user) {
        this.setOwner(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicationPlan)) {
            return false;
        }
        return getId() != null && getId().equals(((MedicationPlan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicationPlan{" +
            "id=" + getId() +
            ", planName='" + getPlanName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", medicationName='" + getMedicationName() + "'" +
            ", colorHex='" + getColorHex() + "'" +
            "}";
    }
}
