package com.ntv.ntvcons_backend.services.OTPPhone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ntv.ntvcons_backend.services.OTPEmail.OtpService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotBlank;

public class SmsRequest {


    @NotBlank
    private final String phoneNumber;

    public SmsRequest(@JsonProperty("phoneNumber") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber.replaceFirst("0","+84");
    }

    public String getMessage() {
        return "Niem Tin Vang OTP : ";
    }

    @Override
    public String toString() {
        return "SmsRequest{" +
                "phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
