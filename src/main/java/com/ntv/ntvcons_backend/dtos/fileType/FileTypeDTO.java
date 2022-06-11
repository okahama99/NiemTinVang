package com.ntv.ntvcons_backend.dtos.fileType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileTypeDTO implements Serializable {
    private Integer fileTypeId;
    private String fileTypeName;
    private String fileTypeDesc;
    private String fileTypeExtension;
    private Boolean isDeleted = false;
}
