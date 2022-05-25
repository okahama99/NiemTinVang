package com.example.ntv.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "externalfile")
public class ExternalFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FileId", nullable = false)
    private int fileId;
    @Basic
    @Column(name = "FileName", nullable = false)
    private String fileName;
    @Basic
    @Column(name = "FileLink", nullable = false, unique = true)
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
