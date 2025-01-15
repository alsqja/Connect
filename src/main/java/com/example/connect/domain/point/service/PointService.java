package com.example.connect.domain.point.service;

import com.example.connect.domain.point.entity.Point;
import com.example.connect.domain.point.repository.PointRepository;
import com.example.connect.domain.pointuse.dto.PointUseListResDto;
import com.example.connect.domain.pointuse.dto.PointUseResDto;
import com.example.connect.domain.pointuse.entity.PointUse;
import com.example.connect.domain.pointuse.repository.PointUseRepository;
import com.example.connect.global.enums.PointUseType;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final PointUseRepository pointUseRepository;

    public BigDecimal nullToZero(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value;
    }

    @Transactional
    public void usePoint(Long userId, Long matchingCount, String details) {
        BigDecimal totalSpendPoint = BigDecimal.valueOf(matchingCount * 50);
        PointUse pointInUse = pointUseRepository.findPointInUse(userId);
        BigDecimal totalHavePoint = totalRemainPoint(userId);

        // 1. 사용하려는 포인트가 현재 보유중인 포인트 보다 작은지 확인
        if (totalSpendPoint.compareTo(totalHavePoint) <= 0) {
            // 1-1. true : 가장 최근 사용 포인트의 남은 양이 사용 요청 포인트 보다 많은지 확인 && 포인트가 사용되었는지 확인
            if (
                    pointInUse.getPointChange().compareTo(totalSpendPoint) >= 0 &&
                            pointInUse.getAmount().compareTo(BigDecimal.ZERO) != 0
            ) {
                // 1-1-1. true : 최근 사용내역에서 포인트 감산
                PointUse pointUse = new PointUse(
                        totalSpendPoint,
                        pointInUse.getPointChange().subtract(totalSpendPoint),
                        details,
                        PointUseType.USE,
                        pointInUse.getPoint()
                );

                if (pointUse.getPointChange().compareTo(BigDecimal.ZERO) == 0) {
                    pointUse.getPoint().pointUpdate(true);
                }

                pointUseRepository.save(pointUse);
            } else {
                // 1-1-2. false : 최근 사용내역 포인트 모두 사용 후 다음 포인트 가져옴
                PointUse updatePoint = pointInUse;

                if (pointInUse.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                    updatePoint = pointUseRepository.findByOldestPoint(userId);
                }

                if (totalSpendPoint.compareTo(updatePoint.getPointChange()) >= 0) {
                    // 소모 포인트가 현제 비교하려는 포인트보다 크거나 같을 경우
                    BigDecimal spend = totalSpendPoint;

                    while (spend.compareTo(BigDecimal.ZERO) > 0) {
                        if (spend.compareTo(totalSpendPoint) != 0) {
                            updatePoint = pointUseRepository.findByOldestPoint(userId);
                        }

                        int compareCheck = BigDecimal.ZERO.compareTo(updatePoint.getPointChange().subtract(spend));

                        PointUse spendPoint = new PointUse(
                                compareCheck > 0 ? updatePoint.getPointChange() : spend,
                                compareCheck > 0 ? BigDecimal.ZERO : updatePoint.getPointChange().subtract(spend),
                                details,
                                PointUseType.USE,
                                updatePoint.getPoint()
                        );

                        if (compareCheck >= 0) {
                            spendPoint.getPoint().pointUpdate(true);
                        }

                        pointUseRepository.save(spendPoint);
                        spend = spend.subtract(updatePoint.getPointChange());
                    }
                } else {
                    // 소모 포인트가 현재 비교하려는 포인트보다 적을 경우
                    PointUse spendPoint = new PointUse(
                            totalSpendPoint,
                            updatePoint.getPointChange().subtract(totalSpendPoint),
                            details,
                            PointUseType.USE,
                            updatePoint.getPoint()
                    );

                    pointUseRepository.save(spendPoint);
                }
            }
        } else {
            // 1-2. false: error message 출력
            throw new BadRequestException(ErrorCode.LACK_POINT);
        }
    }

    /**
     * 포인트 소멸
     *
     * @param pointUseList 소멸 요청될 포인트
     */
    @Transactional
    public void expiredPoint(List<PointUse> pointUseList) {
        for (PointUse pointUse : pointUseList) {
            PointUse updatePointUse = new PointUse(
                    pointUse.getPointChange(),
                    BigDecimal.ZERO,
                    "포인트 기간 만료 소멸",
                    PointUseType.CHANGE,
                    pointUse.getPoint()
            );

            Point updatePoint = updatePointUse.getPoint();
            updatePoint.pointUpdate(true);

            pointRepository.save(updatePoint);
            pointUseRepository.save(updatePointUse);
        }
    }

    public PointUseListResDto getAllPoint(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<PointUse> pointUseList = pointUseRepository.findByUserId(userId, pageable);

        List<PointUseResDto> pointUseResDtos = pointUseList.getContent().stream().map(data ->
                new PointUseResDto(
                        data.getId(),
                        data.getAmount(),
                        data.getPointChange(),
                        data.getDetails(),
                        data.getPointUseType(),
                        data.getCreatedAt(),
                        data.getUpdatedAt()
                )).toList();

        return new PointUseListResDto(page, size, pointUseList.getTotalElements(), pointUseList.getTotalPages(), pointUseResDtos);
    }

    public BigDecimal totalRemainPoint(Long userId) {
        PointUse pointInUse = pointUseRepository.findPointInUse(userId);
        BigDecimal usingPoint = nullToZero(pointInUse.getPointChange());
        BigDecimal purePoint = nullToZero(new BigDecimal(pointRepository.sumAmount(userId)));

        BigDecimal totalRemainPoint = usingPoint.compareTo(BigDecimal.ZERO) == 0 ? purePoint : purePoint.add(usingPoint);

        return totalRemainPoint;
    }
}