package com.ntv.ntvcons_backend.entities.OTPModels;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SmsRequest {
    @NotBlank(message = "To is required")
    private String to;
    @NotBlank(message = "Type is required")
    private String type;
}
