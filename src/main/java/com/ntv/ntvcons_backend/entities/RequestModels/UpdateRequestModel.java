package com.ntv.ntvcons_backend.entities.RequestModels;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class UpdateRequestModel {
    private Long requestId, projectId, requesterId, requestTypeId;
    private String requestDesc;
    private Date updatedAt;
    private Long updatedBy;
}
