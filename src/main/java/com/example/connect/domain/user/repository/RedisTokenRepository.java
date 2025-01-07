package com.example.connect.domain.user.repository;

import com.example.connect.domain.membership.repository.MembershipRepository;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisTokenRepository {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    @Getter
    @Value("${jwt.expiry-millis}")
    private long expiryMillis;

    private static final String REFRESH_TOKEN_KEY = "refreshToken:";
    private static final String SESSION_USER = "session:user:";

    public void saveRefreshToken(String email, String refreshToken, long expiry) {
        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_KEY + email,
                refreshToken,
                expiry,
                TimeUnit.MILLISECONDS
        );
    }

    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY + email);
    }

    public void deleteRefreshToken(String email) {
        redisTemplate.delete(REFRESH_TOKEN_KEY + email);
    }

    public void saveUser(RedisUserDto sessionUser) {

        try {
            String userJson = objectMapper.writeValueAsString(sessionUser);
            redisTemplate.opsForValue().set(
                    SESSION_USER + sessionUser.getEmail(),
                    userJson,
                    expiryMillis,
                    TimeUnit.MILLISECONDS
            );
        } catch (JsonProcessingException e) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
    }

    public RedisUserDto getUser(String email) {

        String userJson = redisTemplate.opsForValue().get(SESSION_USER + email);

        if (userJson == null) {
            return null;
        }

        try {
            RedisUserDto dto = objectMapper.readValue(userJson, RedisUserDto.class);

            System.out.println(dto);

            return dto;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
    }

    public void deleteUser(String email) {
        redisTemplate.delete(SESSION_USER + email);
    }
}
