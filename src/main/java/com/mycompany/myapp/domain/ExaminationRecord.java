package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "examination_record")
public class ExaminationRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "exam_date")
    private LocalDate examDate;

    @Column(name = "original_filename")
    private String originalFilename;

    @Column(name = "stored_filename")
    private String storedFilename;

    @Column(name = "notes")
    private String notes;

    @Column(name = "file", columnDefinition = "bytea") // ή blob ανάλογα με DB
    private byte[] file;

    @Column(name = "file_content_type")
    private String fileContentType;

    @ManyToOne
    private User owner;

    @ManyToOne
    private ExaminationCategory category;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExaminationRecord id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ExaminationRecord title(String title) {
        this.title = title;
        return this;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public ExaminationRecord examDate(LocalDate examDate) {
        this.examDate = examDate;
        return this;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public ExaminationRecord originalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
        return this;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    public ExaminationRecord storedFilename(String storedFilename) {
        this.storedFilename = storedFilename;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ExaminationRecord notes(String notes) {
        this.notes = notes;
        return this;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public ExaminationRecord file(byte[] file) {
        this.file = file;
        return this;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public ExaminationRecord fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public ExaminationRecord owner(User owner) {
        this.owner = owner;
        return this;
    }

    public ExaminationCategory getCategory() {
        return category;
    }

    public void setCategory(ExaminationCategory category) {
        this.category = category;
    }

    public ExaminationRecord category(ExaminationCategory category) {
        this.category = category;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExaminationRecord)) return false;
        ExaminationRecord that = (ExaminationRecord) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    // μπορείς να έχεις και το toString() μετά
    @Override
    public String toString() {
        return (
            "ExaminationRecord{" +
            "id=" +
            id +
            // ...
            '}'
        );
    }
}
