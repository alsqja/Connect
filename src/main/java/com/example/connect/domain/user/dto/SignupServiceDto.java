package com.example.connect.domain.user.dto;

import com.example.connect.domain.user.entity.User;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignupServiceDto {

    private final String email;
    private final String password;
    private final String name;
    private final String birth;
    private final Gender gender;
    private final Boolean isActiveMatching;
    private final String profileUrl;
    private final UserRole role;

    public User toUser() {
        return new User(
                email,
                password,
                name,
                birth,
                gender,
                profileUrl,
                isActiveMatching,
                role
        );
    }
}
