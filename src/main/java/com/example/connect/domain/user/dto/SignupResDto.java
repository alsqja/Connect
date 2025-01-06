package com.example.connect.domain.user.dto;

import com.example.connect.domain.user.entity.User;
import com.example.connect.global.common.dto.BaseDtoType;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class SignupResDto implements BaseDtoType {

    private final Long id;
    private final String email;
    private final String name;
    private final String birth;
    private final Gender gender;
    private final String profileUrl;
    private final UserRole role;
    private final Boolean isActiveMatching;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public SignupResDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.birth = user.getBirth();
        this.gender = user.getGender();
        this.profileUrl = user.getProfileUrl();
        this.role = user.getRole();
        this.isActiveMatching = user.getIsActiveMatching();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
