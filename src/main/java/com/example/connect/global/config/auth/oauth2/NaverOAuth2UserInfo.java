package com.example.connect.global.config.auth.oauth2;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get("response")); // "response" 필드 사용
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("profile_image");
    }

    @Override
    public String getGender() {
        return (String) attributes.get("gender"); // 성별 (M, F)
    }

    @Override
    public String getBirth() {
        return (String) attributes.get("birthyear") + attributes.get("birthday").toString().split("-")[0] + attributes.get("birthday").toString().split("-")[1]; // YYYY-MM-DD
    }
}
