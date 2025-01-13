package com.example.connect.domain.schedulesubcategory.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleSubCategoryResDto implements BaseDtoType {

    private final Long id;
    private final Long subCategoryId;
    private final String subCategoryName;
    private final String description;
    private final String subCategoryImageUrl;
}
