package com.example.connect.domain.file.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UrlReqDto {

    private final String fileName;

    @JsonCreator
    public UrlReqDto(String fileName) {
        this.fileName = fileName;
    }
}
