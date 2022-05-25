package com.example.ntv.Database.Entities;
import com.example.ntv.Database.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@Entity
@Table(name = "filetype")
public class FileType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FileTypeId", nullable = false)
    private int fileTypeId;
    @Basic
    @Column(name = "FileTypeName", nullable = false)
    private String fileTypeName;
    @Basic
    @Column(name = "FileTypeDesc")
    private String fileTypeDesc;
    @Basic
    @Column(name = "FileTypeExtension", nullable = false)
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
