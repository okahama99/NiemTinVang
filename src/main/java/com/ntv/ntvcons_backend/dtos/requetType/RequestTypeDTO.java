package com.ntv.ntvcons_backend.dtos.requetType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTypeDTO implements Serializable {
    private Integer requestTypeId;
    private String requestTypeName;
    private String requestTypeDesc;
    private Boolean isDeleted;
}
