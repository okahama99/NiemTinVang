package com.ntv.ntvcons_backend.services.OTP;

import java.util.Date;
import java.util.regex.Pattern;

public interface ValidateService {
    Boolean isValidWithRegex(String value, Pattern pattern);

    Boolean isValidNumberPhone(String numberPhone);

    Boolean isNotValidNumberPhone(String numberPhone);

    Boolean isValidEmail(String email);

    Boolean isNotValidEmail(String email);

    Boolean isValidPassword(String password);

    Boolean isNotValidPassword(String password);

    Boolean isValidBirthday(Date birthday);

    Boolean isValidNumberPhoneForOtp(String numberPhone);

    Boolean isValidCampaignType(String type);

    Boolean isValidStartDateEndDate(Date startDate, Date endDate);

    Boolean isValidCampaignTime(Date startCampaignDate, Date endCampaignDate, Date startProjectDate, Date endProjectDate);

    Boolean isValidCampaignTargetNumber(Long targetNumber, String campaignType);

    Boolean isValidParticipateType(String participateType, String campaignType);

    Boolean isValidDonateAmount(Long donateAmount);

    Boolean isValidApprovementType(String type, String role);
}
