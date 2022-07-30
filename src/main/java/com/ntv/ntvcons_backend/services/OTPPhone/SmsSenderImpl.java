package com.ntv.ntvcons_backend.services.OTPPhone;

import com.ntv.ntvcons_backend.services.OTPEmail.OtpService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsSenderImpl implements SmsSender{

    @Autowired
    private OtpService otpService;

    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public SmsSenderImpl(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    @Override
    public void smsSender(SmsRequest smsRequest) {
            MessageCreator creator = Message.creator(new PhoneNumber(smsRequest.getPhoneNumber()),
                    new PhoneNumber(twilioConfiguration.getTrialNumber()),
                    (smsRequest.getMessage()+otpService.generatePhoneOTP(smsRequest.getPhoneNumber()))
            ); //build sms

            creator.create(); // send sms

    }
}
