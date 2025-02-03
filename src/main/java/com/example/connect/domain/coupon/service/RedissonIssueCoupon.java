package com.example.connect.domain.coupon.service;

import com.example.connect.domain.couponuser.dto.CouponUserResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonIssueCoupon {
    private final RedissonClient redissonClient;
    private final CouponService couponService;


    public CouponUserResDto issueCoupon(Long id, Long userId) {
        RLock lock = redissonClient.getLock(id + "st_coupon_lock_key_" + userId);
        CouponUserResDto couponUserResDto = null;

        try {
            boolean isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS);

            if (isLocked) {
                couponUserResDto = couponService.issueCouponUser(id, userId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return couponUserResDto;
    }
}
