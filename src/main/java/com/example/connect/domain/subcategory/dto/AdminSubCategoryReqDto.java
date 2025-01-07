package com.example.connect.domain.subcategory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminSubCategoryReqDto {

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "Url을 입력해 주세요.")
    private String imageUrl;

    @NotNull(message = "카테고리 Id를 입력해 주세요.")
    private final Long categoryId;
}
