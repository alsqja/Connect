package com.example.connect.domain.pointuse.repository;

import com.example.connect.domain.pointuse.entity.PointUse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PointUseRepository extends JpaRepository<PointUse, Long> {
    @Query("select pu from PointUse pu join pu.point p where p.user.id = :userId and p.isZero = false order by p.createdAt asc")
    List<PointUse> findByOldestPoint(Long userId, Pageable pageable);

    @Query("select pu from PointUse pu join pu.point p where p.user.id = :userId and pu.pointChange > 0 order by pu.createdAt desc, pu.point.id desc")
    List<PointUse> findPointInUse(Long userId, Pageable pageable);

    @Query("select pu from PointUse pu join pu.point p where p.createdAt < :expiredDate and p.isZero = false and pu.createdAt = (select max(ipu.createdAt) from PointUse ipu where ipu.point.id = p.id)")
    List<PointUse> findExpiredPoints(LocalDateTime expiredDate);

    @Query("select (count(pu) > 1) from PointUse pu where pu.point.payment.id = :paymentId")
    Boolean isPointUse(Long paymentId);

    @Query("select pu from PointUse pu where pu.point.user.id = :userId order by pu.createdAt desc")
    Page<PointUse> findByUserId(Long userId, Pageable pageable);
}
