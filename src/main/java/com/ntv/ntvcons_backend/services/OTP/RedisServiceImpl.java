package com.ntv.ntvcons_backend.services.OTP;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService{
    @Override
    public void add(String key, String data, Long expiredTime) {

    }

    @Override
    public String get(String key) throws JsonProcessingException {
        return null;
    }

    @Override
    public void remove(String key) {

    }
}
