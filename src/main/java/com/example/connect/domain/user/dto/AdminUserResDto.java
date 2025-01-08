package com.example.connect.domain.user.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.MembershipType;
import com.example.connect.global.enums.UserRole;
import com.example.connect.global.enums.UserStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AdminUserResDto implements BaseDtoType {

    private final Long id;
    private final String name;
    private final String birth;
    private final Gender gender;
    private final String profileUrl;
    private final UserStatus status;
    private final Boolean isActiveMatching;
    private final UserRole role;
    private final MembershipType memberType;
    private final Long reportCount;
    private final LocalDate expiredDate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean isDeleted;
}
