package com.example.connect.domain.subcategory.dto;

import com.example.connect.domain.category.entity.Category;
import com.example.connect.domain.subcategory.entity.SubCategory;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AdminSubCategoryResDto implements BaseDtoType {

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final Long categoryId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static AdminSubCategoryResDto toDto(SubCategory subCategory) {
        return new AdminSubCategoryResDto(
                subCategory.getId(),
                subCategory.getName(),
                subCategory.getImageUrl(),
                subCategory.getCategory().getId(),
                subCategory.getCreatedAt(),
                subCategory.getUpdatedAt()
        );
    }
}
