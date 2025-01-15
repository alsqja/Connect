package com.example.connect.domain.user.service;

import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.domain.membership.repository.MembershipRepository;
import com.example.connect.domain.user.dto.NaverTokenResponse;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.user.dto.UserTokenResDto;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.common.dto.TokenDto;
import com.example.connect.global.config.auth.oauth2.NaverOAuth2UserInfo;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.UserRole;
import com.example.connect.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaverAuthService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String userInfoUri;

    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public UserTokenResDto handleNaverLogin(String code, String state) {

        RestTemplate restTemplate = new RestTemplate();

        String url = tokenUri + "?grant_type=authorization_code&client_id=" + clientId +
                "&client_secret=" + clientSecret + "&code=" + code + "&state=" + state;

        NaverTokenResponse tokenResponse = restTemplate.getForObject(url, NaverTokenResponse.class);

        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            throw new IllegalStateException("Failed to retrieve access token from Naver");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenResponse.getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("Failed to retrieve user information from Naver");
        }

        Map<String, Object> attributes = (Map<String, Object>) response.getBody();
        NaverOAuth2UserInfo userInfo = new NaverOAuth2UserInfo(attributes);

        String email = userInfo.getEmail();
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User(
                    email,
                    null,
                    userInfo.getName(),
                    userInfo.getBirth(),
                    userInfo.getGender().equals("M") ? Gender.MAN : Gender.WOMAN,
                    userInfo.getImageUrl(),
                    true,
                    UserRole.USER
            );
            return userRepository.save(newUser);
        });

        if (user.getId() != null) {
            user.updateUserFields(userInfo.getName(), userInfo.getImageUrl(), user.getIsActiveMatching());
        }

        Membership membership = membershipRepository.findByUserIdAndExpiredDateAfter(user.getId(), LocalDate.now()).orElse(null);

        RedisUserDto sessionUser;

        if (membership == null) {
            sessionUser = new RedisUserDto(user);
        } else {
            sessionUser = new RedisUserDto(user, membership);
        }

        TokenDto tokens = jwtProvider.generateToken(sessionUser);

        return new UserTokenResDto(sessionUser, tokens);
    }
}
