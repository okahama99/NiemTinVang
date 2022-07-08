package com.ntv.ntvcons_backend.dtos.fileType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileTypeReadDTO extends BaseReadDTO {
    private Long fileTypeId;
    private String fileTypeName;
    private String fileTypeDesc;
    private String fileTypeExtension;
}
