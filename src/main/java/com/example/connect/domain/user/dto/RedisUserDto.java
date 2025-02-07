package com.example.connect.domain.user.dto;

import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.MembershipType;
import com.example.connect.global.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RedisUserDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String birth;
    private Gender gender;
    private String profileUrl;
    private Boolean isActiveMatching;
    private UserRole role;
    private MembershipType membershipType;
    private LocalDate expiredDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RedisUserDto(User user, Membership membership) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.password = user.getPassword();
        this.birth = user.getBirth();
        this.gender = user.getGender();
        this.role = user.getRole();
        this.profileUrl = user.getProfileUrl();
        this.isActiveMatching = user.getIsActiveMatching();
        this.membershipType = membership.getType();
        this.expiredDate = membership.getExpiredDate();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    public RedisUserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.password = user.getPassword();
        this.birth = user.getBirth();
        this.gender = user.getGender();
        this.role = user.getRole();
        this.profileUrl = user.getProfileUrl();
        this.isActiveMatching = user.getIsActiveMatching();
        this.membershipType = null;
        this.expiredDate = null;
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    public void updateMembership(MembershipType membershipType, LocalDate expiredDate) {
        this.membershipType = membershipType;
        this.expiredDate = expiredDate;
    }
}
