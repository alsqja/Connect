package com.example.connect.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminCategoryReqDto {

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "Url을 입력해 주세요.")
    private String imageUrl;
}
