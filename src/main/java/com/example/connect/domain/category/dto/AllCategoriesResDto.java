package com.example.connect.domain.category.dto;

import com.example.connect.domain.subcategory.dto.AdminSubCategoryResDto;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AllCategoriesResDto implements BaseDtoType {

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<AdminSubCategoryResDto> subCategories;
}
