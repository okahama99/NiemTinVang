package com.ntv.ntvcons_backend.services.OTP;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsModel {
    private String to;
    private String message;
}
