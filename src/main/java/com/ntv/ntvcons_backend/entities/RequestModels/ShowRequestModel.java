package com.ntv.ntvcons_backend.entities.RequestModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ShowRequestModel {
    private Long requestId, projectId, requesterId, requestTypeId, verifierId;
    private String requestDesc, verifyNote, requesterName, requestTypeName, verifierName, projectName;
    private LocalDateTime requestDate, verifyDate;
    private Boolean isVerified, isApproved;

    private Long createdBy, updatedBy;
    private Date createdAt, updatedAt;
    private Double totalPage;
}
