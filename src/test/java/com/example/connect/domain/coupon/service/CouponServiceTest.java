package com.example.connect.domain.coupon.service;

import com.example.connect.domain.coupon.entity.Coupon;
import com.example.connect.domain.coupon.repository.CouponRepository;
import com.example.connect.domain.couponuser.repository.CouponUserRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Profile("test")
@Rollback(false)
public class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponUserRepository couponUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedissonIssueCoupon redissonIssueCoupon;

    private Coupon coupon;

    @BeforeEach
    void init() {
        coupon = new Coupon(
                "test coupon",
                "coupon description",
                10,
                5,
                LocalDate.now().plusDays(5),
                LocalDateTime.now().minusDays(1)
        );
        couponRepository.saveAndFlush(coupon);
    }

    @Test
    @DisplayName("쿠폰 한명 발급")
    void issueCouponOne() {
        User user1 = new User(
                "email@email.com",
                passwordEncoder.encode("Password1!"),
                "testUser",
                "19970714",
                Gender.WOMAN,
                "https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512",
                true,
                UserRole.USER
        );

        userRepository.save(user1);
        couponService.issueCouponUser(coupon.getId(), user1.getId());

        Long count = couponUserRepository.countCouponUseId(coupon.getId());
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("쿠폰 동시성 제어 테스트")
    void createCoupon() throws InterruptedException {
        Set<Long> userIds = new HashSet<>();
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            User user = new User(
                    "email" + i + "@email.com",
                    passwordEncoder.encode("Password1!"),
                    "testUser" + i,
                    "19970714",
                    Gender.WOMAN,
                    "https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512",
                    true,
                    UserRole.USER
            );
            userRepository.save(user);
            userIds.add(user.getId());
        }

        for (Long userId : userIds) {
            executorService.submit(() -> {
                try {
                    redissonIssueCoupon.issueCoupon(coupon.getId(), userId);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        long coupons = couponUserRepository.countCouponUseId(coupon.getId());
        assertThat(coupons).isEqualTo(10);
    }
}
