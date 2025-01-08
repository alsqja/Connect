package com.example.connect.domain.user.dto;

import com.example.connect.domain.user.entity.UserAdminOnly;
import com.example.connect.global.common.dto.BaseDtoType;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.UserRole;
import com.example.connect.global.enums.UserStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserResDto implements BaseDtoType {

    private final Long id;
    private final String name;
    private final String email;
    private final String birth;
    private final Gender gender;
    private final String profileUrl;
    private final Boolean isActiveMatching;
    private final UserStatus status;
    private final UserRole role;
    private final Boolean isDeleted;

    public UpdateUserResDto(UserAdminOnly user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.birth = user.getBirth();
        this.gender = user.getGender();
        this.profileUrl = user.getProfileUrl();
        this.isActiveMatching = user.getIsActiveMatching();
        this.status = user.getStatus();
        this.role = user.getRole();
        this.isDeleted = user.getIsDeleted();
    }
}
