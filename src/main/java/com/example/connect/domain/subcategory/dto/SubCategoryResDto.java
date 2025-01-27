package com.example.connect.domain.subcategory.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubCategoryResDto implements BaseDtoType {

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final Long categoryId;
    private final Long registeredCount;
}
