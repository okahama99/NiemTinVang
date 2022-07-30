package com.ntv.ntvcons_backend.services.OTPPhone;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfiguration {
    private String accountSID="ACecdf6f85c3cb8ba6fa555a4fac145d4c";
    private String authToken="706af510f8ec73d9dda11e52733855f7";
    private String trialNumber="+15862815848";

    public TwilioConfiguration() {
    }

    public String getAccountSID() {
        return accountSID;
    }

    public void setAccountSID(String accountSID) {
        this.accountSID = accountSID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getTrialNumber() {
        return trialNumber;
    }

    public void setTrialNumber(String trialNumber) {
        this.trialNumber = trialNumber;
    }
}
