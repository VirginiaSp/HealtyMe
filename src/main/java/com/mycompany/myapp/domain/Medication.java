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
 * A Medication.
 */
@Entity
@Table(name = "medication")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Lob
    @Column(name = "notes")
    private String notes;

    @Size(max = 200)
    @Column(name = "dosage", length = 200)
    private String dosage;

    @Size(max = 100)
    @Column(name = "frequency", length = 100)
    private String frequency;

    @Size(max = 500)
    @Column(name = "side_effects", length = 500)
    private String sideEffects;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "last_taken")
    private LocalDate lastTaken;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_medication__categories",
        joinColumns = @JoinColumn(name = "medication_id"),
        inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "createdBy", "medications" }, allowSetters = true)
    private Set<MedicationCategory> categories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medication id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Medication name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRating() {
        return this.rating;
    }

    public Medication rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return this.notes;
    }

    public Medication notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDosage() {
        return this.dosage;
    }

    public Medication dosage(String dosage) {
        this.setDosage(dosage);
        return this;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return this.frequency;
    }

    public Medication frequency(String frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getSideEffects() {
        return this.sideEffects;
    }

    public Medication sideEffects(String sideEffects) {
        this.setSideEffects(sideEffects);
        return this;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Medication createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getLastTaken() {
        return this.lastTaken;
    }

    public Medication lastTaken(LocalDate lastTaken) {
        this.setLastTaken(lastTaken);
        return this;
    }

    public void setLastTaken(LocalDate lastTaken) {
        this.lastTaken = lastTaken;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Medication owner(User user) {
        this.setOwner(user);
        return this;
    }

    public Set<MedicationCategory> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<MedicationCategory> medicationCategories) {
        this.categories = medicationCategories;
    }

    public Medication categories(Set<MedicationCategory> medicationCategories) {
        this.setCategories(medicationCategories);
        return this;
    }

    public Medication addCategories(MedicationCategory medicationCategory) {
        this.categories.add(medicationCategory);
        return this;
    }

    public Medication removeCategories(MedicationCategory medicationCategory) {
        this.categories.remove(medicationCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medication)) {
            return false;
        }
        return getId() != null && getId().equals(((Medication) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medication{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rating=" + getRating() +
            ", notes='" + getNotes() + "'" +
            ", dosage='" + getDosage() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", sideEffects='" + getSideEffects() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastTaken='" + getLastTaken() + "'" +
            "}";
    }
}
