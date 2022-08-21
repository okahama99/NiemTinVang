package com.ntv.ntvcons_backend.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public abstract class BaseReadDTO implements Serializable {
    @JsonInclude(Include.NON_NULL) /* if null => no return JSON */
    private Status status;

    private Long createdBy;
    private String createdAt;

    private Long updatedBy;
    private String updatedAt;

    @JsonInclude(Include.NON_NULL) /* if null => no return JSON */
    private List<ExternalFileReadDTO> fileList;

    @JsonInclude(Include.NON_NULL) /* if null => no return JSON */
    private ExternalFileReadDTO file;

    @JsonInclude(Include.NON_NULL) /* if null => no return JSON */
    private Integer totalPage;
}
