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
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;
    private final PointUseRepository pointUseRepository;

    private BigDecimal nullToZero(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value;
    }

    /**
     * 포인트 사용
     *
     * @param matchingCount 매칭 돌릴 횟수
     * @param details       상세 설명
     */
    @Override
    @Transactional
    public void usePoint(Long userId, Long matchingCount, String details) {
        Pageable pageable = PageRequest.of(0, 1);
        BigDecimal totalSpendPoint = BigDecimal.valueOf(matchingCount * 50);
        BigDecimal totalHavePoint = totalRemainPoint(userId);
        List<PointUse> pointUseList = pointUseRepository.findPointInUse(userId, pageable);

        if (totalSpendPoint.compareTo(totalHavePoint) > 0 || pointUseList.isEmpty()) {
            throw new BadRequestException(ErrorCode.LACK_POINT);
        }

        PointUse pointInUse = pointUseList.get(0);

        if (
                nullToZero(pointInUse.getPointChange()).compareTo(totalSpendPoint) >= 0 &&
                        nullToZero(pointInUse.getAmount()).compareTo(BigDecimal.ZERO) != 0
        ) {
            Boolean isPointUpdate = pointInUse.getPointChange().compareTo(BigDecimal.ZERO) == 0;

            pointInUseSave(pointInUse, totalSpendPoint, pointInUse.getPointChange().subtract(totalSpendPoint), details, isPointUpdate);

        } else {
            PointUse oldestPoint = pointInUse;

            if (nullToZero(pointInUse.getAmount()).compareTo(BigDecimal.ZERO) == 0) {
                oldestPoint = getOldestPoint(userId, pageable);
            }

            if (totalSpendPoint.compareTo(oldestPoint.getPointChange()) >= 0) {
                BigDecimal spend = totalSpendPoint;

                while (spend.compareTo(BigDecimal.ZERO) > 0) {

                    if (spend.compareTo(totalSpendPoint) != 0) {
                        oldestPoint = getOldestPoint(userId, pageable);
                    }

                    BigDecimal minAmount = oldestPoint.getPointChange().min(spend);
                    BigDecimal pointChange = BigDecimal.ZERO.compareTo(oldestPoint.getPointChange().subtract(spend)) > 0
                            ? BigDecimal.ZERO : oldestPoint.getPointChange().subtract(spend);
                    Boolean isPointUpdate = BigDecimal.ZERO.compareTo(oldestPoint.getPointChange().subtract(spend)) >= 0;

                    pointInUseSave(oldestPoint, minAmount, pointChange, details, isPointUpdate);

                    spend = spend.subtract(oldestPoint.getPointChange());
                }

            } else {
                BigDecimal subPoint = oldestPoint.getPointChange().subtract(totalSpendPoint);

                pointInUseSave(oldestPoint, totalSpendPoint, subPoint, details, false);
            }
        }
    }

    /**
     * 포인트 사용으로 인한 사용내역 저장
     *
     * @param pointInUse    사용중인 포인트 내역
     * @param amount        포인트 사용 양
     * @param pointChange   포인트 소모 양
     * @param details       포인트 사용 내역
     * @param isPointUpdate 포인트 상태 업데이트 여부
     */
    private void pointInUseSave(
            PointUse pointInUse, BigDecimal amount, BigDecimal pointChange, String details, Boolean isPointUpdate
    ) {
        PointUse pointUse = new PointUse(
                amount,
                pointChange,
                details,
                PointUseType.USE,
                pointInUse.getPoint()
        );

        if (isPointUpdate) {
            pointUse.getPoint().pointUpdate(true);
        }

        pointUseRepository.save(pointUse);
    }

    /**
     * 생성한지 가장 오래된 포인트 조회
     */
    private PointUse getOldestPoint(Long userId, Pageable pageable) {
        List<PointUse> oldestPointList = pointUseRepository.findByOldestPoint(userId, pageable);

        if (oldestPointList.isEmpty()) {
            throw new BadRequestException(ErrorCode.LACK_POINT);
        } else {
            return oldestPointList.get(0);
        }
    }

    /**
     * 포인트 소멸
     *
     * @param pointUseList 소멸 요청될 포인트
     */
    @Override
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

    /**
     * 포인트 전체 조회
     */
    @Override
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

    /**
     * 유저 소유 포인트
     */
    @Override
    public BigDecimal totalRemainPoint(Long userId) {
        Pageable pageable = PageRequest.of(0, 1);
        List<PointUse> pointUseList = pointUseRepository.findPointInUse(userId, pageable);

        if (!pointUseList.isEmpty()) {
            PointUse pointInUse = pointUseList.get(0);
            Long sumAmount = pointRepository.sumAmount(userId);

            BigDecimal usingPoint = nullToZero(pointInUse.getPointChange());
            BigDecimal purePoint = new BigDecimal(sumAmount == null ? 0 : sumAmount);
            BigDecimal totalRemainPoint = usingPoint.compareTo(BigDecimal.ZERO) == 0 ? purePoint : purePoint.add(usingPoint);

            return totalRemainPoint;
        } else {
            return BigDecimal.ZERO;
        }
    }
}