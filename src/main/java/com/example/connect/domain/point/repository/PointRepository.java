package com.example.connect.domain.point.repository;

import com.example.connect.domain.point.entity.Point;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointRepository extends JpaRepository<Point, Long> {
    default Point findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query("select sum(p.amount) from Point p where p.isZero = false and (select count(pu) from PointUse pu where pu.point.id = p.id) = 1 and p.user.id = :userId")
    Long sumAmount(Long userId);
}
