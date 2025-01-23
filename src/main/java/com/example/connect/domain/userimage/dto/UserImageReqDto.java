package com.example.connect.domain.userimage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserImageReqDto {

    @NotBlank(message = "사진을 입력해 주세요.")
    private final String url;

    private final String description;
}
