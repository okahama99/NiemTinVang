package com.ntv.ntvcons_backend.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntv.ntvcons_backend.constants.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseReadDTO implements Serializable {
    @JsonInclude(Include.NON_NULL)
    private Status status;

    private Long createdBy;
    private LocalDateTime createdAt;

    private Long updatedBy;
    private LocalDateTime updatedAt;

    @JsonInclude(Include.NON_NULL)
    private Integer totalPage;
}
