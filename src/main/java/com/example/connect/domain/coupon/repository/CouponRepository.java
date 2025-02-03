package com.example.connect.domain.coupon.repository;

import com.example.connect.domain.coupon.entity.Coupon;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CustomCouponRepository {
    default Coupon findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    List<Coupon> findByExpiredDateIsLessThanEqual(LocalDate now);

    Coupon findByName(String name);
}
