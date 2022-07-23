package com.ntv.ntvcons_backend.entities.OTPModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SmsInfo {
    private String to;
    private String message;
}
