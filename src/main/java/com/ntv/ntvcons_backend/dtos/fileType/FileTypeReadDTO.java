package com.ntv.ntvcons_backend.dtos.fileType;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileTypeReadDTO implements Serializable {
    private Long fileTypeId;
    private String fileTypeName;
    private String fileTypeDesc;
    private String fileTypeExtension;

    /* TODO: to be replace with status */
//    private Boolean isDeleted = false;

    /* If null, then no show in json */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalPage;
}
