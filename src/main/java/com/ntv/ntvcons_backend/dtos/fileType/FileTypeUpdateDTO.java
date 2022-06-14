package com.ntv.ntvcons_backend.dtos.fileType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileTypeUpdateDTO implements Serializable {
    private Long fileTypeId;
    private String fileTypeName;
    private String fileTypeDesc;
    private String fileTypeExtension;
    /* TODO: to be replace with status */
    private final Boolean isDeleted = false;
}
