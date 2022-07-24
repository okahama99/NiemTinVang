package com.ntv.ntvcons_backend.services.OTP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ntv.ntvcons_backend.entities.OTPModels.BaseResponse;

public interface OTPService {
    String generateOtpSixDigits();

//    BaseResponse checkOtpMatchNumberPhone(OtpRequest otpRequest) throws JsonProcessingException;
}
