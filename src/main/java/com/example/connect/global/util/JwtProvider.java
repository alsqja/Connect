package com.example.connect.global.util;

import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.user.repository.RedisTokenRepository;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.common.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Getter
    @Value("${jwt.expiry-millis}")
    private long expiryMillis;

    @Getter
    @Value("${jwt.refresh-expiry-millis}")
    private long refreshExpiryMillis;

    private final UserRepository userRepository;
    private final RedisTokenRepository redisTokenRepository;

    public TokenDto generateToken(RedisUserDto sessionUser) throws EntityNotFoundException {

        String accessToken = this.generateTokenBy(sessionUser);
        String refreshToken = this.generateRefreshToken(sessionUser);

        redisTokenRepository.saveUser(sessionUser);
        redisTokenRepository.saveRefreshToken(sessionUser.getEmail(), refreshToken, this.refreshExpiryMillis);

        return new TokenDto(accessToken, refreshToken);
    }

    public String getUsername(String token) {
        Claims claims = this.getClaims(token);
        return claims.getSubject();
    }

    public boolean validToken(String token) throws JwtException {
        try {
            return !this.tokenExpired(token);
        } catch (MalformedJwtException e) {
            log.error("잘못된 토큰: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 토큰: {}", e.getMessage());
        }

        return false;
    }

    private String generateTokenBy(RedisUserDto sessionUser) throws EntityNotFoundException {

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + this.expiryMillis);

        return Jwts.builder()
                .subject(sessionUser.getEmail())
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claim("role", sessionUser.getRole())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    private Claims getClaims(String token) {
        if (!StringUtils.hasText(token)) {
            throw new MalformedJwtException("토큰이 비어 있습니다.");
        }

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean tokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return this.resolveClaims(token, Claims::getExpiration);
    }

    private <T> T resolveClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.getClaims(token);
        return claimsResolver.apply(claims);
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(RedisUserDto sessionUser) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + this.refreshExpiryMillis);

        return Jwts.builder()
                .subject(sessionUser.getEmail())
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    // 리프레시 토큰 검증
    public boolean validRefreshToken(String token, String email) {
        try {
            String savedRefreshToken = redisTokenRepository.getRefreshToken(email);

            if (!savedRefreshToken.equals(token)) {
                log.error("없는 refresh token");
                return false;
            }

            Claims claims = this.getClaims(token);

            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            log.error("잘못된 refresh token: {}", e.getMessage());
            return false;
        }
    }

}
