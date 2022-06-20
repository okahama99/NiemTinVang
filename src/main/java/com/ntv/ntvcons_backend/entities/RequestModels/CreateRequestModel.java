package com.ntv.ntvcons_backend.entities.RequestModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateRequestModel {
    private Long projectId, requesterId, requestTypeId;
    private String requestDesc;
    private String requestDate;
}
