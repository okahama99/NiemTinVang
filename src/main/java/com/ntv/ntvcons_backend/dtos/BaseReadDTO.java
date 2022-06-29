package com.ntv.ntvcons_backend.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseReadDTO implements Serializable {
    /* TODO: to be replace with status */
//    private Boolean isDeleted = false;

    private Long createdBy;
    private LocalDateTime createdAt;

    private Long updatedBy;
    private LocalDateTime updatedAt;

    @JsonInclude(Include.NON_NULL)
    private Integer totalPage;
}
