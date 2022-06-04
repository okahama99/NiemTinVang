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
public class FileType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FileTypeId", nullable = false)
    private int fileTypeId;
    @Basic
    @Column(name = "FileTypeName", nullable = false, length = 500)
    private String fileTypeName;
    @Basic
    @Column(name = "FileTypeDesc", nullable = true, length = 500)
    private String fileTypeDesc;
    @Basic
    @Column(name = "FileTypeExtension", nullable = false, length = 500)
    private String fileTypeExtension;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileType fileType = (FileType) o;
        return fileTypeId == fileType.fileTypeId && Objects.equals(fileTypeName, fileType.fileTypeName) && Objects.equals(fileTypeDesc, fileType.fileTypeDesc) && Objects.equals(fileTypeExtension, fileType.fileTypeExtension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileTypeId, fileTypeName, fileTypeDesc, fileTypeExtension);
    }
}
