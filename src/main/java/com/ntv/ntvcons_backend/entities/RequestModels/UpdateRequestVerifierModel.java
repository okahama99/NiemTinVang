package com.ntv.ntvcons_backend.entities.RequestModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateRequestVerifierModel {
    private Long requestId, verifierId;
    private String verifyDate;
    private String verifierNote;
    private Boolean isVerified;
}
