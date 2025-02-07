package com.example.connect.domain.userimage.dto;

import com.example.connect.domain.userimage.entity.UserImage;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class UserImagePageResDto implements BaseDtoType {

    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final List<UserImageResDto> data;

    public UserImagePageResDto(Page<UserImage> userImagePage) {
        this.page = userImagePage.getNumber() + 1;
        this.size = userImagePage.getSize();
        this.totalElements = userImagePage.getTotalElements();
        this.totalPages = userImagePage.getTotalPages();
        this.data = userImagePage.getContent().stream().map(UserImageResDto::new).toList();
    }
}
