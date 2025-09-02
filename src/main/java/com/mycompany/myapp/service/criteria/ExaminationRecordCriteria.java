package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.ExaminationRecord} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ExaminationRecordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /examination-records?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExaminationRecordCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private LocalDateFilter examDate;

    private StringFilter originalFilename;

    private StringFilter storedFilename;

    private StringFilter notes;

    private LongFilter ownerId;

    private LongFilter categoryId;

    private Boolean distinct;

    public ExaminationRecordCriteria() {}

    public ExaminationRecordCriteria(ExaminationRecordCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.examDate = other.optionalExamDate().map(LocalDateFilter::copy).orElse(null);
        this.originalFilename = other.optionalOriginalFilename().map(StringFilter::copy).orElse(null);
        this.storedFilename = other.optionalStoredFilename().map(StringFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.ownerId = other.optionalOwnerId().map(LongFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ExaminationRecordCriteria copy() {
        return new ExaminationRecordCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public LocalDateFilter getExamDate() {
        return examDate;
    }

    public Optional<LocalDateFilter> optionalExamDate() {
        return Optional.ofNullable(examDate);
    }

    public LocalDateFilter examDate() {
        if (examDate == null) {
            setExamDate(new LocalDateFilter());
        }
        return examDate;
    }

    public void setExamDate(LocalDateFilter examDate) {
        this.examDate = examDate;
    }

    public StringFilter getOriginalFilename() {
        return originalFilename;
    }

    public Optional<StringFilter> optionalOriginalFilename() {
        return Optional.ofNullable(originalFilename);
    }

    public StringFilter originalFilename() {
        if (originalFilename == null) {
            setOriginalFilename(new StringFilter());
        }
        return originalFilename;
    }

    public void setOriginalFilename(StringFilter originalFilename) {
        this.originalFilename = originalFilename;
    }

    public StringFilter getStoredFilename() {
        return storedFilename;
    }

    public Optional<StringFilter> optionalStoredFilename() {
        return Optional.ofNullable(storedFilename);
    }

    public StringFilter storedFilename() {
        if (storedFilename == null) {
            setStoredFilename(new StringFilter());
        }
        return storedFilename;
    }

    public void setStoredFilename(StringFilter storedFilename) {
        this.storedFilename = storedFilename;
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

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public Optional<LongFilter> optionalCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            setCategoryId(new LongFilter());
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
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
        final ExaminationRecordCriteria that = (ExaminationRecordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(examDate, that.examDate) &&
            Objects.equals(originalFilename, that.originalFilename) &&
            Objects.equals(storedFilename, that.storedFilename) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, examDate, originalFilename, storedFilename, notes, ownerId, categoryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExaminationRecordCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalExamDate().map(f -> "examDate=" + f + ", ").orElse("") +
            optionalOriginalFilename().map(f -> "originalFilename=" + f + ", ").orElse("") +
            optionalStoredFilename().map(f -> "storedFilename=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalOwnerId().map(f -> "ownerId=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
