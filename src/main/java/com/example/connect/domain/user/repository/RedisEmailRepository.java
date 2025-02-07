package com.example.connect.domain.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisEmailRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String EMAIL_CODE_KEY = "code:email:";
    private static final String EMAIL_STATUS_KEY = "email:status:";

    public void saveEmailCode(String email, String code) {
        redisTemplate.opsForValue().set(
                EMAIL_CODE_KEY + email,
                code,
                3,
                TimeUnit.MINUTES
        );
    }

    public String getEmailCode(String email) {
        return redisTemplate.opsForValue().get(EMAIL_CODE_KEY + email);
    }

    public void saveEmailStatus(String email, String status) {
        redisTemplate.opsForValue().set(EMAIL_STATUS_KEY + email, status);
    }

    public String getEmailStatus(String email) {
        return redisTemplate.opsForValue().get("email:status:" + email);
    }

    public void deleteEmailStatus(String email) {
        redisTemplate.delete(EMAIL_STATUS_KEY + email);
    }
}
