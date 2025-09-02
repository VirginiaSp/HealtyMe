package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;

public class ExaminationRecordCriteria implements Serializable {

    private LongFilter id;

    private LocalDateFilter examDate;

    public ExaminationRecordCriteria() {}

    public ExaminationRecordCriteria(ExaminationRecordCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.examDate = other.examDate == null ? null : other.examDate.copy();
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

    public LocalDateFilter getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateFilter examDate) {
        this.examDate = examDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExaminationRecordCriteria)) return false;
        ExaminationRecordCriteria that = (ExaminationRecordCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(examDate, that.examDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, examDate);
    }

    @Override
    public String toString() {
        return (
            "ExaminationRecordCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (examDate != null ? "examDate=" + examDate + ", " : "") +
            "}"
        );
    }
}
