package com.ntv.ntvcons_backend.services.OTP2;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsSenderImpl implements SmsSender{

    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public SmsSenderImpl(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    @Override
    public void smsSender(SmsRequest smsRequest) {
        if(isPhoneNumberValid(smsRequest.getPhoneNumber()))
        {
            MessageCreator creator = Message.creator(new PhoneNumber(smsRequest.getPhoneNumber()),
                    new PhoneNumber(twilioConfiguration.getTrialNumber()),
                    smsRequest.getMessage()
            ); //build sms

            creator.create(); // send sms
        }else{
            throw new IllegalArgumentException("Phone Number [ " + smsRequest.getPhoneNumber() + " ] is not a valid phone number");
        }
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        // implement phone number validate
        return true;
    }
}
