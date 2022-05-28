package com.ntv.ntvcons_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ExternalFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FileId", nullable = false)
    private int fileId;
    @Basic
    @Column(name = "FileName", nullable = false, length = 500)
    private String fileName;
    @Basic
    @Column(name = "FileLink", nullable = false, length = 500)
    private String fileLink;
    @Basic
    @Column(name = "FileTypeId", nullable = false)
    private int fileTypeId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternalFile that = (ExternalFile) o;
        return fileId == that.fileId && fileTypeId == that.fileTypeId && Objects.equals(fileName, that.fileName) && Objects.equals(fileLink, that.fileLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, fileName, fileLink, fileTypeId);
    }
}
