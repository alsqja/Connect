package com.example.connect.domain.userimage.dto;

import com.example.connect.domain.userimage.entity.UserImage;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserImageResDto implements BaseDtoType {

    private final Long id;
    private final Long userId;
    private final String url;
    private final String description;

    public UserImageResDto(UserImage userImage) {
        this.id = userImage.getId();
        this.userId = userImage.getUser().getId();
        this.url = userImage.getUrl();
        this.description = userImage.getDescription();
    }
}
