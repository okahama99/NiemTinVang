//package com.ntv.ntvcons_backend.services.OTP;
//
//import com.ntv.ntvcons_backend.entities.OTPModels.BaseResponse;
//import com.ntv.ntvcons_backend.entities.OTPModels.SmsInfo;
//import com.ntv.ntvcons_backend.entities.OTPModels.SmsRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SmsServiceImpl implements SmsService{
//    @Autowired
//    private RedisService redisService;
//
//    @Autowired
//    private OTPService otpService;
//
//    @Autowired
//    private ValidateService validateService;
//
//    private final BusinessDataConfiguration businessDataConfiguration;
//    private final MessageSource messageSource;
//
//    @Override
//    public void send(SmsInfo smsInfo) throws Exception {
//        Twilio.init(businessDataConfiguration.getTwilioAccountSid(), businessDataConfiguration.getTwilioAuthToken());
//        Message message = Message.creator(
//                new PhoneNumber(smsInfo.getTo()),
//                new PhoneNumber(businessDataConfiguration.getTwilioPhoneNumber()),
//                smsInfo.getMessage())
//                .create();
//    }
//
//    @Override
//    public BaseResponse buildData(SmsRequest smsRequest) {
//        if (Constant.SEND_OTP.equals(smsRequest.getType())) {
//            if (!validateService.isValidNumberPhoneForOtp(smsRequest.getTo())) {
//                return new BaseResponse(Status.NUMBER_PHONE_NOT_VALID_OTP, Status.getMessage(Status.NUMBER_PHONE_NOT_VALID_OTP, null, messageSource));
//            }
//            String otpNumber = otpService.generateOtpSixDigits();
//            String redisOtpKey = RedisKey.CONNETO_OTP_ + smsRequest.getTo();
//            redisService.add(redisOtpKey, otpNumber, Timeout.OTP_EXPIRED);
//            String message = String.format(Constant.SEND_OTP_MESSAGE, otpNumber);
//            SmsInfo smsInfo = new SmsInfo(smsRequest.getTo(), message);
//            try {
//                send(smsInfo);
//            } catch (Exception ex) {
//                log.error("Send OTP catch exception: {}", ex.getMessage());
//                throw new ApiException(Status.ERROR);
//            }
//        }
//        return new BaseResponse(Status.SUCCESS, Status.getMessage(Status.SUCCESS, null, messageSource));
//    }
//}
