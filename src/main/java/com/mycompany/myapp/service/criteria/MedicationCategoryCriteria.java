// Replace your MedicationCategoryCriteria.java with this complete version:
// src/main/java/com/mycompany/myapp/service/criteria/MedicationCategoryCriteria.java

package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicationCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;
    private StringFilter name;
    private StringFilter description;
    private StringFilter color;
    private StringFilter icon;
    private LocalDateFilter createdDate;
    private LongFilter createdById;
    private LongFilter medicationsId;
    private Boolean distinct;

    public MedicationCategoryCriteria() {}

    public MedicationCategoryCriteria(MedicationCategoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.color = other.optionalColor().map(StringFilter::copy).orElse(null);
        this.icon = other.optionalIcon().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(LocalDateFilter::copy).orElse(null);
        this.createdById = other.optionalCreatedById().map(LongFilter::copy).orElse(null);
        this.medicationsId = other.optionalMedicationsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MedicationCategoryCriteria copy() {
        return new MedicationCategoryCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getColor() {
        return color;
    }

    public Optional<StringFilter> optionalColor() {
        return Optional.ofNullable(color);
    }

    public StringFilter color() {
        if (color == null) {
            setColor(new StringFilter());
        }
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    public StringFilter getIcon() {
        return icon;
    }

    public Optional<StringFilter> optionalIcon() {
        return Optional.ofNullable(icon);
    }

    public StringFilter icon() {
        if (icon == null) {
            setIcon(new StringFilter());
        }
        return icon;
    }

    public void setIcon(StringFilter icon) {
        this.icon = icon;
    }

    public LocalDateFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<LocalDateFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public LocalDateFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new LocalDateFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(LocalDateFilter createdDate) {
        this.createdDate = createdDate;
    }

    // This is the method that was missing!
    public LongFilter getCreatedById() {
        return createdById;
    }

    public Optional<LongFilter> optionalCreatedById() {
        return Optional.ofNullable(createdById);
    }

    public LongFilter createdById() {
        if (createdById == null) {
            setCreatedById(new LongFilter());
        }
        return createdById;
    }

    public LongFilter getOwnerId() {
        return getCreatedById();
    }

    public LongFilter ownerId() {
        return createdById();
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public LongFilter getMedicationsId() {
        return medicationsId;
    }

    public Optional<LongFilter> optionalMedicationsId() {
        return Optional.ofNullable(medicationsId);
    }

    public LongFilter medicationsId() {
        if (medicationsId == null) {
            setMedicationsId(new LongFilter());
        }
        return medicationsId;
    }

    public void setMedicationsId(LongFilter medicationsId) {
        this.medicationsId = medicationsId;
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
        final MedicationCategoryCriteria that = (MedicationCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(color, that.color) &&
            Objects.equals(icon, that.icon) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(medicationsId, that.medicationsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, color, icon, createdDate, createdById, medicationsId, distinct);
    }

    @Override
    public String toString() {
        return (
            "MedicationCategoryCriteria{" +
            optionalId().map(f -> "id=" + f).orElse("") +
            optionalName().map(f -> ", name=" + f).orElse("") +
            optionalDescription().map(f -> ", description=" + f).orElse("") +
            optionalColor().map(f -> ", color=" + f).orElse("") +
            optionalIcon().map(f -> ", icon=" + f).orElse("") +
            optionalCreatedDate().map(f -> ", createdDate=" + f).orElse("") +
            optionalCreatedById().map(f -> ", createdById=" + f).orElse("") +
            optionalMedicationsId().map(f -> ", medicationsId=" + f).orElse("") +
            optionalDistinct().map(f -> ", distinct=" + f).orElse("") +
            "}"
        );
    }
}
