package com.example.connect.domain.userimage.dto;

import com.example.connect.domain.userimage.entity.UserImage;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserImageDetailResDto implements BaseDtoType {

    private final Long id;
    private final String url;
    private final String description;
    private final String userName;
    private final Long userId;
    private final String userProfileUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public UserImageDetailResDto(UserImage userImage) {
        this.id = userImage.getId();
        this.url = userImage.getUrl();
        this.description = userImage.getDescription();
        this.userName = userImage.getUser().getName();
        this.userId = userImage.getUser().getId();
        this.userProfileUrl = userImage.getUser().getProfileUrl();
        this.createdAt = userImage.getCreatedAt();
        this.updatedAt = userImage.getUpdatedAt();
    }
}
