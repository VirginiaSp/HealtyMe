package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.MedicationPlanDose} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MedicationPlanDoseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /medication-plan-doses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicationPlanDoseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter timeOfDay;

    private StringFilter notes;

    private LongFilter ownerId;

    private LongFilter planId;

    private Boolean distinct;

    public MedicationPlanDoseCriteria() {}

    public MedicationPlanDoseCriteria(MedicationPlanDoseCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.timeOfDay = other.optionalTimeOfDay().map(StringFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.ownerId = other.optionalOwnerId().map(LongFilter::copy).orElse(null);
        this.planId = other.optionalPlanId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MedicationPlanDoseCriteria copy() {
        return new MedicationPlanDoseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTimeOfDay() {
        return timeOfDay;
    }

    public Optional<StringFilter> optionalTimeOfDay() {
        return Optional.ofNullable(timeOfDay);
    }

    public StringFilter timeOfDay() {
        if (timeOfDay == null) {
            setTimeOfDay(new StringFilter());
        }
        return timeOfDay;
    }

    public void setTimeOfDay(StringFilter timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public Optional<StringFilter> optionalNotes() {
        return Optional.ofNullable(notes);
    }

    public StringFilter notes() {
        if (notes == null) {
            setNotes(new StringFilter());
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public Optional<LongFilter> optionalOwnerId() {
        return Optional.ofNullable(ownerId);
    }

    public LongFilter ownerId() {
        if (ownerId == null) {
            setOwnerId(new LongFilter());
        }
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getPlanId() {
        return planId;
    }

    public Optional<LongFilter> optionalPlanId() {
        return Optional.ofNullable(planId);
    }

    public LongFilter planId() {
        if (planId == null) {
            setPlanId(new LongFilter());
        }
        return planId;
    }

    public void setPlanId(LongFilter planId) {
        this.planId = planId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MedicationPlanDoseCriteria that = (MedicationPlanDoseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(timeOfDay, that.timeOfDay) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(planId, that.planId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeOfDay, notes, ownerId, planId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicationPlanDoseCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTimeOfDay().map(f -> "timeOfDay=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalOwnerId().map(f -> "ownerId=" + f + ", ").orElse("") +
            optionalPlanId().map(f -> "planId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
