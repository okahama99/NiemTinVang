package com.ntv.ntvcons_backend.entities.RequestModels;

import com.ntv.ntvcons_backend.entities.RequestDetail;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShowRequestModel {
    private Long requestId, projectId, requesterId, requestTypeId, verifierId;
    private String requestName, requestDesc, verifyNote, requesterName, requestTypeName, verifierName, projectName;
    private LocalDateTime requestDate, verifyDate;
    private Boolean isVerified, isApproved;

    private List<RequestDetail> requestDetailList;

    private Long createdBy, updatedBy;
    private LocalDateTime createdAt, updatedAt;
    private Double totalPage;
}
