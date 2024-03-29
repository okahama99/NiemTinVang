package com.ntv.ntvcons_backend.dtos.request;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestReadDTO extends BaseReadDTO {
    private Long requestId;
    private Long projectId;
    private RequestTypeReadDTO requestType;
    private UserReadDTO requester;
    private String requestName;
    private String requestDate;
    private String requestDesc;

    private List<RequestDetailReadDTO> requestDetailList;

    private UserReadDTO verifier;
    private Boolean isVerified;
    private String verifyDate;
    private String verifyNote;
    private Boolean isApproved;
}
