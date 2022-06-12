package com.ntv.ntvcons_backend.dtos.requestType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTypeUpdateDTO implements Serializable {
    private Long requestTypeId;
    private String requestTypeName;
    private String requestTypeDesc;
    /* TODO: to be replace with status */
    private final Boolean isDeleted = false;
}
