package com.ntv.ntvcons_backend.dtos.requestType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTypeReadDTO extends BaseReadDTO {
    private Long requestTypeId;
    private String requestTypeName;
    private String requestTypeDesc;
}