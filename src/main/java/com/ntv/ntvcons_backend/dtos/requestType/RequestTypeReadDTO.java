package com.ntv.ntvcons_backend.dtos.requestType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTypeReadDTO implements Serializable {
    private Long requestTypeId;
    private String requestTypeName;
    private String requestTypeDesc;

    /* TODO: to be replace with status */
//    private Boolean isDeleted = false;

    /* If null, then no show in json */
    @JsonInclude(Include.NON_NULL)
    private Integer totalPage;
}