package com.ntv.ntvcons_backend.dtos.request;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.project.ProjectReadDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailCreateDTO;
import com.ntv.ntvcons_backend.dtos.requestDetail.RequestDetailReadDTO;
import com.ntv.ntvcons_backend.dtos.requestType.RequestTypeReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestReadDTO extends BaseReadDTO {
    private Long requestId;
    private Long projectId;
    private RequestTypeReadDTO requestType;
    private UserReadDTO Requester;
    private LocalDateTime requestDate;
    private String requestDesc;

    private List<RequestDetailReadDTO> requestDetailList;

    private UserReadDTO verifier;
    private Boolean isVerified;
    private LocalDateTime verifyDate;
    private String verifyNote;
    private Boolean isApproved;
}
