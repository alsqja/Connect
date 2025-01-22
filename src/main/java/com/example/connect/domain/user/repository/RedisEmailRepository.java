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
}
