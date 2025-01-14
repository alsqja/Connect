package com.example.connect.domain.pointuse.repository;

import com.example.connect.domain.pointuse.entity.PointUse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointUseRepository extends JpaRepository<PointUse, Long> {
    // 포인트를 다 사용하지 않은 가장 생성된지 오래된 포인트 조회
    @Query("select pu from PointUse pu where pu.point.id = (select p.id from Point p where p.user.id = :userId and p.isZero = false order by p.createdAt asc limit 1)")
    PointUse findByOldestPoint(Long userId);

    // 사용중이던 포인트가 있으면 그것부터 조회
    @Query("select pu from PointUse pu join pu.point p where p.user.id = :userId and pu.pointChange > 0 order by pu.createdAt desc, pu.point.id desc limit 1")
    PointUse findPointInUse(Long userId);
}
