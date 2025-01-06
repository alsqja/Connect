package com.example.connect.domain.user.dto;

import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserReqDto {


    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력해 주세요.")
    private final String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
            message = "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다."
    )
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private final String password;

    @NotBlank(message = "이름을 입력해 주세요.")
    private final String name;

    @NotBlank(message = "생년월일을 입력해 주세요.")
    @Pattern(regexp = "^\\d{4}\\d{2}\\d{2}$", message = "생년월일을 확인해 주세요.")
    private final String birth;

    @NotNull(message = "성별을 입력해 주세요.")
    private final Gender gender;

    @NotBlank(message = "주소를 입력해 주세요.")
    private final String address;

    @NotNull(message = "주소를 입력해 주세요.")
    private final Double latitude;

    @NotNull(message = "주소를 입력해 주세요.")
    private final Double longitude;

    @NotNull(message = "매칭 가능 여부를 입력해 주세요.")
    private final Boolean isActiveMatching;

    @NotBlank(message = "프로필 사진을 입력해 주세요.")
    private final String profileUrl;

    private final UserRole role;

    public SignupServiceDto toSignupServiceDto() {
        return new SignupServiceDto(
                email,
                password,
                name,
                birth,
                gender,
                address,
                latitude,
                longitude,
                isActiveMatching,
                profileUrl,
                role
        );
    }
}
