package com.ntv.ntvcons_backend.dtos.requestType;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTypeReadDTO extends BaseReadDTO {
    private Long requestTypeId;
    private String requestTypeName;
    private String requestTypeDesc;
}