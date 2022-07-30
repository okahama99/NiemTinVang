package com.ntv.ntvcons_backend.services.OTPEmail;


public interface EmailService {
    void send(String to, String email, String subject);
}
