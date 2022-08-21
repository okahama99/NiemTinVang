package com.ntv.ntvcons_backend.dtos.externalFile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.constants.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalFileReadDTO {
    private Long fileId;
    private String fileName;
    private String fileLink;
    private FileType fileType;

    /* Copy from BaseReadDTO to avoid having file & fileList */
    @JsonInclude(JsonInclude.Include.NON_NULL) /* if null => no return JSON */
    private Status status;

    private Long createdBy;
    private String createdAt;

    private Long updatedBy;
    private String updatedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL) /* if null => no return JSON */
    private Integer totalPage;
}
