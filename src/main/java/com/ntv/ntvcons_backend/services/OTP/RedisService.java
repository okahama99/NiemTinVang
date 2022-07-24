package com.ntv.ntvcons_backend.services.OTP;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface RedisService {
    void add(String key, String data, Long expiredTime);

    String get(String key) throws JsonProcessingException;

    void remove(String key);
}
