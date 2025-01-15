package com.example.connect.domain.pointuse.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import com.example.connect.global.enums.PointUseType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PointUseResDto implements BaseDtoType {
    private final Long id;
    private final BigDecimal amount;
    private final BigDecimal pointChange;
    private final String details;
    private final PointUseType pointUseType;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
