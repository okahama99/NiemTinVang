package com.ntv.ntvcons_backend.services.OTP;

import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


@Component
public class SmsService {

    //twilio.com/console
    private final String ACCOUNT_SID ="ACbdd3a733780b5497cad5a41dc5b577b8";

    private final String AUTH_TOKEN = "aa35023988e6a949e6baf43e89faa251";

    private final String FROM_NUMBER = "+84 389 037 476";

    public void send(SmsModel sms) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber(sms.getTo()), new PhoneNumber(FROM_NUMBER), sms.getMessage())
                .create();
        System.out.println("Here is my ID: "+message.getSid());// Unique resource ID created to manage this transaction

    }

    public void receive(MultiValueMap<String, String> smscallback) {
    }

}
