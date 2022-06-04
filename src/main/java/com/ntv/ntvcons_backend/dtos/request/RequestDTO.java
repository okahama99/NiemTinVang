package com.ntv.ntvcons_backend.dtos.request;

import com.ntv.ntvcons_backend.dtos.project.ProjectDTO;
import com.ntv.ntvcons_backend.dtos.requetType.RequestTypeDTO;
import com.ntv.ntvcons_backend.dtos.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO implements Serializable {
    private Integer requestId;
    private ProjectDTO projectId;
    private RequestTypeDTO requestType;
    private UserDTO Requester;
    private Instant requestDate;
    private String requestDesc;
    private UserDTO verifier;
    private Boolean isVerified;
    private Instant verifyDate;
    private String verifyNote;
    private Boolean isApproved;
    private Boolean isDeleted;
}