package com.example.connect.global.config;

import com.example.connect.domain.coupon.entity.Coupon;
import com.example.connect.domain.coupon.repository.CouponRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.UserRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Profile("prod")
@Component
@RequiredArgsConstructor
public class DeployDataInitializer {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        userRepository.findByEmailOrElseThrow("admin@example.com");
        User user = new User("admin@gmail.com", passwordEncoder.encode("Password1!"), "관리자", "10000814", Gender.MAN, "https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512", false, UserRole.ADMIN);
        userRepository.save(user);

        Coupon coupon = new Coupon("가입 축하 쿠폰", "가입을 축하드립니다! (매칭 무료 5회 쿠폰)", 0, 5, LocalDate.of(9999, 12, 31), LocalDateTime.now());
        couponRepository.save(coupon);
    }
}
