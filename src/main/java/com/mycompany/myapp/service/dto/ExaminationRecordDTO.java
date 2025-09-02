package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ExaminationRecordDTO implements Serializable {

    private Long id;

    private LocalDate examDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExaminationRecordDTO)) return false;
        ExaminationRecordDTO that = (ExaminationRecordDTO) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExaminationRecordDTO{" + "id=" + id + ", examDate=" + examDate + '}';
    }
}
