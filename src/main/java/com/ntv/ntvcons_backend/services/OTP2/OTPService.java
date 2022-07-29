package com.ntv.ntvcons_backend.services.OTP2;

import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class OTPService {

    private final SmsSenderImpl smsSenderImpl;

    @Autowired
    public OTPService(SmsSenderImpl smsSenderImpl) {
        this.smsSenderImpl = smsSenderImpl;
    }

    public void smsSender(SmsRequest smsRequest){
        smsSenderImpl.smsSender(smsRequest);
    }
}
