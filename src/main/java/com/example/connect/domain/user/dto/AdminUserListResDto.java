package com.example.connect.domain.user.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AdminUserListResDto implements BaseDtoType {

    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final List<AdminUserResDto> data;
}
