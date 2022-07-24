package com.ntv.ntvcons_backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntv.ntvcons_backend.entities.OTPModels.BaseResponse;
import com.ntv.ntvcons_backend.entities.OTPModels.SmsRequest;
import com.ntv.ntvcons_backend.services.OTP.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sms")
public class OTPController {
 //   @Autowired
    //private SmsService smsService;
//
//    private ObjectMapper objectMapper;
//
//    @PostMapping("send")
//    public ResponseEntity<BaseResponse> send(@RequestBody SmsRequest smsRequest) throws JsonProcessingException {
//        log.info("START POST----/accounts/login: {}", objectMapper.writeValueAsString(smsRequest));
//        long start = System.currentTimeMillis();
//
//        BaseResponse response = smsService.buildData(smsRequest);
//
//        long duration = System.currentTimeMillis() - start;
//        log.info("END POST----/accounts/is_exist response: {} - run `{}`ms", objectMapper.writeValueAsString(response), duration);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
