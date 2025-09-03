package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ExaminationRecordDTO implements Serializable {

    private Long id;
    private String title;
    private LocalDate examDate;
    private String originalFilename;
    private String storedFilename;
    private String notes;
    private byte[] file;
    private String fileContentType;
    private Long ownerId;
    private Long categoryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
        return (
            "ExaminationRecordDTO{" +
            "id=" +
            id +
            ", title='" +
            title +
            '\'' +
            ", examDate=" +
            examDate +
            ", originalFilename='" +
            originalFilename +
            '\'' +
            ", storedFilename='" +
            storedFilename +
            '\'' +
            ", notes='" +
            notes +
            '\'' +
            ", file=" +
            (file != null ? "[...]" : "null") +
            ", fileContentType='" +
            fileContentType +
            '\'' +
            ", ownerId=" +
            ownerId +
            ", categoryId=" +
            categoryId +
            '}'
        );
    }
}
