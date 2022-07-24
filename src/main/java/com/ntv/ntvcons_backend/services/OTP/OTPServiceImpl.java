package com.ntv.ntvcons_backend.services.OTP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ntv.ntvcons_backend.entities.OTPModels.BaseResponse;
import org.springframework.stereotype.Service;

@Service
public class OTPServiceImpl implements OTPService{
    @Override
    public String generateOtpSixDigits() {
        return null;
    }

//    @Override
//    public BaseResponse checkOtpMatchNumberPhone(OtpRequest otpRequest) throws JsonProcessingException {
//        return null;
//    }
}
