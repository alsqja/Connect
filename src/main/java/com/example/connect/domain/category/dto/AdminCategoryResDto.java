package com.example.connect.domain.category.dto;


import com.example.connect.domain.category.entity.Category;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AdminCategoryResDto implements BaseDtoType {

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public AdminCategoryResDto (Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.imageUrl = category.getImageUrl();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
    }
}
