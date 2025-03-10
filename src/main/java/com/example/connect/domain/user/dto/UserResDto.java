package com.example.connect.domain.user.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.MembershipType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserResDto implements BaseDtoType {

    private final Long id;
    private final String email;
    private final String name;
    private final String birth;
    private final Gender gender;
    private final String profileUrl;
    private final Boolean isActiveMatching;
    private final MembershipType membershipType;
    private final LocalDate expiredDate;
    private final Boolean isActiveMembership;
    private final BigDecimal point;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public UserResDto(RedisUserDto user, BigDecimal point, Boolean isActiveMembership) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.birth = user.getBirth();
        this.gender = user.getGender();
        this.profileUrl = user.getProfileUrl();
        this.isActiveMatching = user.getIsActiveMatching();
        this.membershipType = user.getMembershipType();
        this.expiredDate = user.getExpiredDate();
        this.point = point;
        this.isActiveMembership = isActiveMembership;
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
