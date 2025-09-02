package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ExaminationRecord} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExaminationRecordDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private LocalDate examDate;

    @Lob
    private byte[] file;

    private String fileContentType;

    private String originalFilename;

    private String storedFilename;

    @Size(max = 1000)
    private String notes;

    @NotNull
    private UserDTO owner;

    private ExaminationCategoryDTO category;

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

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public ExaminationCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ExaminationCategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExaminationRecordDTO)) {
            return false;
        }

        ExaminationRecordDTO examinationRecordDTO = (ExaminationRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examinationRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExaminationRecordDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", examDate='" + getExamDate() + "'" +
            ", file='" + getFile() + "'" +
            ", originalFilename='" + getOriginalFilename() + "'" +
            ", storedFilename='" + getStoredFilename() + "'" +
            ", notes='" + getNotes() + "'" +
            ", owner=" + getOwner() +
            ", category=" + getCategory() +
            "}";
    }
}
