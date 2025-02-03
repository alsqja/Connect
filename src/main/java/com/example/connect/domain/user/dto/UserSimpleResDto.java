package com.example.connect.domain.user.dto;

import com.example.connect.domain.user.entity.User;
import com.example.connect.global.common.dto.BaseDtoType;
import com.example.connect.global.enums.Gender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSimpleResDto implements BaseDtoType {

    private final Long id;
    private final String name;
    private final String birth;
    private final Gender gender;
    private final String profileUrl;
    private final double rateAvg;

    public UserSimpleResDto(User user, double rateAvg) {
        this.id = user.getId();
        this.name = user.getName();
        this.birth = user.getBirth();
        this.gender = user.getGender();
        this.profileUrl = user.getProfileUrl();
        this.rateAvg = rateAvg;
    }
}
