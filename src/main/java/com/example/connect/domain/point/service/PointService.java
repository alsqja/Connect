package com.example.connect.domain.point.service;

import com.example.connect.domain.pointuse.dto.PointUseListResDto;
import com.example.connect.domain.pointuse.entity.PointUse;

import java.math.BigDecimal;
import java.util.List;

public interface PointService {
    void usePoint(Long userId, Long matchingCount, String details);

    void expiredPoint(List<PointUse> pointUseList);

    PointUseListResDto getAllPoint(Long userId, int page, int size);

    BigDecimal totalRemainPoint(Long userId);
}
