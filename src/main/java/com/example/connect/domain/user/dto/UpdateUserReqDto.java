package com.example.connect.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserReqDto {

    private final String name;
    private final String oldPassword;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
            message = "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다."
    )
    private final String newPassword;

    private final String profileUrl;
    private final Boolean isActiveMatching;

    public UpdateUserServiceDto toServiceDto(RedisUserDto sessionUser) {
        return new UpdateUserServiceDto(
                sessionUser,
                name,
                oldPassword,
                newPassword,
                profileUrl,
                isActiveMatching
        );
    }
}
