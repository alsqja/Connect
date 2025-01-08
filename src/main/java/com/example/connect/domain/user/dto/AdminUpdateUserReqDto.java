package com.example.connect.domain.user.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import com.example.connect.global.enums.UserRole;
import com.example.connect.global.enums.UserStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminUpdateUserReqDto implements BaseDtoType {

    private final UserRole role;
    private final Boolean isDeleted;
    private final UserStatus status;
}
