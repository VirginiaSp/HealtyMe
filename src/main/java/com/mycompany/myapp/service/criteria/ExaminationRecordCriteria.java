package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

public class ExaminationRecordCriteria implements Serializable {

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
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.examDate = other.examDate == null ? null : other.examDate.copy();
        this.originalFilename = other.originalFilename == null ? null : other.originalFilename.copy();
        this.storedFilename = other.storedFilename == null ? null : other.storedFilename.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.distinct = other.distinct;
    }

    public ExaminationRecordCriteria copy() {
        return new ExaminationRecordCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public LocalDateFilter getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateFilter examDate) {
        this.examDate = examDate;
    }

    public StringFilter getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(StringFilter originalFilename) {
        this.originalFilename = originalFilename;
    }

    public StringFilter getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(StringFilter storedFilename) {
        this.storedFilename = storedFilename;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExaminationRecordCriteria)) return false;
        ExaminationRecordCriteria that = (ExaminationRecordCriteria) o;
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

    @Override
    public String toString() {
        return "ExaminationRecordCriteria{}";
    }
}
