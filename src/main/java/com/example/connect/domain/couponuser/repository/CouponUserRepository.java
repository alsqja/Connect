package com.example.connect.domain.couponuser.repository;

import com.example.connect.domain.couponuser.entity.CouponUser;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CouponUserRepository extends JpaRepository<CouponUser, Long>, CustomCouponUserRepository {
    default CouponUser findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query("select count(cu) from CouponUser cu where cu.coupon.id = :couponId")
    Long countCouponUseId(Long couponId);

    boolean existsByCouponIdAndUserId(Long couponId, Long userId);

    List<CouponUser> findByExpiredDateIsLessThanEqual(LocalDate expiredDate);
}
