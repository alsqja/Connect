package com.example.connect.domain.user.dto;

import com.example.connect.domain.address.entity.Address;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.common.dto.BaseDtoType;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.MembershipType;
import com.example.connect.global.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserResDto implements BaseDtoType {

    private final Long id;
    private final String email;
    private final String name;
    private final String birth;
    private final Gender gender;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final String profileUrl;
    private final UserRole role;
    private final Boolean isActiveMatching;
    private final MembershipType memberType;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public UserResDto(User user, Address address) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.birth = user.getBirth();
        this.gender = user.getGender();
        this.address = address.getAddress();
        this.latitude = address.getLatitude();
        this.longitude = address.getLongitude();
        this.profileUrl = user.getProfileUrl();
        this.role = user.getRole();
        this.isActiveMatching = user.getIsActiveMatching();
        this.memberType = MembershipType.NORMAL;
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
