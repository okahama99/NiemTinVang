package com.ntv.ntvcons_backend.services.OTP;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Pattern;

@Service
public class ValidateServiceImpl implements ValidateService{
    @Override
    public Boolean isValidWithRegex(String value, Pattern pattern) {
        return null;
    }

    @Override
    public Boolean isValidNumberPhone(String numberPhone) {
        return null;
    }

    @Override
    public Boolean isNotValidNumberPhone(String numberPhone) {
        return null;
    }

    @Override
    public Boolean isValidEmail(String email) {
        return null;
    }

    @Override
    public Boolean isNotValidEmail(String email) {
        return null;
    }

    @Override
    public Boolean isValidPassword(String password) {
        return null;
    }

    @Override
    public Boolean isNotValidPassword(String password) {
        return null;
    }

    @Override
    public Boolean isValidBirthday(Date birthday) {
        return null;
    }

    @Override
    public Boolean isValidNumberPhoneForOtp(String numberPhone) {
        return null;
    }

    @Override
    public Boolean isValidCampaignType(String type) {
        return null;
    }

    @Override
    public Boolean isValidStartDateEndDate(Date startDate, Date endDate) {
        return null;
    }

    @Override
    public Boolean isValidCampaignTime(Date startCampaignDate, Date endCampaignDate, Date startProjectDate, Date endProjectDate) {
        return null;
    }

    @Override
    public Boolean isValidCampaignTargetNumber(Long targetNumber, String campaignType) {
        return null;
    }

    @Override
    public Boolean isValidParticipateType(String participateType, String campaignType) {
        return null;
    }

    @Override
    public Boolean isValidDonateAmount(Long donateAmount) {
        return null;
    }

    @Override
    public Boolean isValidApprovementType(String type, String role) {
        return null;
    }
}
