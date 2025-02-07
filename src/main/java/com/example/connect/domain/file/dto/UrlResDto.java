package com.example.connect.domain.file.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UrlResDto implements BaseDtoType {

    private final String url;
    private final String fileName;
}
