package com.ntv.ntvcons_backend.services.OTP;

import com.ntv.ntvcons_backend.entities.OTPModels.BaseResponse;
import com.ntv.ntvcons_backend.entities.OTPModels.SmsInfo;
import com.ntv.ntvcons_backend.entities.OTPModels.SmsRequest;

public interface SmsService {
    void send(SmsInfo smsInfo) throws Exception;

    BaseResponse buildData(SmsRequest smsRequest);
}
