package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.MedicationPlan} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MedicationPlanResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /medication-plans?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicationPlanCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter planName;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private StringFilter medicationName;

    private StringFilter colorHex;

    private LongFilter dosesId;

    private LongFilter ownerId;

    private Boolean distinct;

    public MedicationPlanCriteria() {}

    public MedicationPlanCriteria(MedicationPlanCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.planName = other.optionalPlanName().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.medicationName = other.optionalMedicationName().map(StringFilter::copy).orElse(null);
        this.colorHex = other.optionalColorHex().map(StringFilter::copy).orElse(null);
        this.dosesId = other.optionalDosesId().map(LongFilter::copy).orElse(null);
        this.ownerId = other.optionalOwnerId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MedicationPlanCriteria copy() {
        return new MedicationPlanCriteria(this);
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

    public StringFilter getPlanName() {
        return planName;
    }

    public Optional<StringFilter> optionalPlanName() {
        return Optional.ofNullable(planName);
    }

    public StringFilter planName() {
        if (planName == null) {
            setPlanName(new StringFilter());
        }
        return planName;
    }

    public void setPlanName(StringFilter planName) {
        this.planName = planName;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public Optional<LocalDateFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            setEndDate(new LocalDateFilter());
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public StringFilter getMedicationName() {
        return medicationName;
    }

    public Optional<StringFilter> optionalMedicationName() {
        return Optional.ofNullable(medicationName);
    }

    public StringFilter medicationName() {
        if (medicationName == null) {
            setMedicationName(new StringFilter());
        }
        return medicationName;
    }

    public void setMedicationName(StringFilter medicationName) {
        this.medicationName = medicationName;
    }

    public StringFilter getColorHex() {
        return colorHex;
    }

    public Optional<StringFilter> optionalColorHex() {
        return Optional.ofNullable(colorHex);
    }

    public StringFilter colorHex() {
        if (colorHex == null) {
            setColorHex(new StringFilter());
        }
        return colorHex;
    }

    public void setColorHex(StringFilter colorHex) {
        this.colorHex = colorHex;
    }

    public LongFilter getDosesId() {
        return dosesId;
    }

    public Optional<LongFilter> optionalDosesId() {
        return Optional.ofNullable(dosesId);
    }

    public LongFilter dosesId() {
        if (dosesId == null) {
            setDosesId(new LongFilter());
        }
        return dosesId;
    }

    public void setDosesId(LongFilter dosesId) {
        this.dosesId = dosesId;
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
        final MedicationPlanCriteria that = (MedicationPlanCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(planName, that.planName) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(medicationName, that.medicationName) &&
            Objects.equals(colorHex, that.colorHex) &&
            Objects.equals(dosesId, that.dosesId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, planName, startDate, endDate, medicationName, colorHex, dosesId, ownerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicationPlanCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPlanName().map(f -> "planName=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalMedicationName().map(f -> "medicationName=" + f + ", ").orElse("") +
            optionalColorHex().map(f -> "colorHex=" + f + ", ").orElse("") +
            optionalDosesId().map(f -> "dosesId=" + f + ", ").orElse("") +
            optionalOwnerId().map(f -> "ownerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
