package com.example.connect.global.config;

import com.example.connect.domain.address.entity.Address;
import com.example.connect.domain.address.repository.AddressRepository;
import com.example.connect.domain.banner.entity.Banner;
import com.example.connect.domain.banner.repository.BannerRepository;
import com.example.connect.domain.category.entity.Category;
import com.example.connect.domain.category.repository.CategoryRepository;
import com.example.connect.domain.coupon.entity.Coupon;
import com.example.connect.domain.coupon.repository.CouponRepository;
import com.example.connect.domain.couponuser.entity.CouponUser;
import com.example.connect.domain.couponuser.repository.CouponUserRepository;
import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.match.repository.MatchRepository;
import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.domain.membership.repository.MembershipRepository;
import com.example.connect.domain.point.entity.Point;
import com.example.connect.domain.point.repository.PointRepository;
import com.example.connect.domain.report.entity.Report;
import com.example.connect.domain.report.repository.ReportRepository;
import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.schedule.repository.ScheduleRepository;
import com.example.connect.domain.schedulesubcategory.entity.ScheduleSubCategory;
import com.example.connect.domain.schedulesubcategory.repository.ScheduleSubCategoryRepository;
import com.example.connect.domain.subcategory.entity.SubCategory;
import com.example.connect.domain.subcategory.repository.SubCategoryRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.enums.CouponUserStatus;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.MatchStatus;
import com.example.connect.global.enums.MembershipType;
import com.example.connect.global.enums.UserRole;
import com.example.connect.global.enums.UserStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;
    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;
    private final MatchRepository matchRepository;
    private final MembershipRepository membershipRepository;
    private final PointRepository pointRepository;
    private final ReportRepository reportRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleSubCategoryRepository scheduleSubCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @PostConstruct
    public void init() {
        User user1 = new User(
                "email@email.com",
                "password",
                "eunyoung",
                "19970714",
                Gender.WOMAN,
                "https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512",
                true,
                UserStatus.NORMAL,
                UserRole.USER
        );
        User savedUser1 = userRepository.save(user1);

        User user2 = new User(
                "email2@email.com",
                "password",
                "minbeom",
                "19970814",
                Gender.MAN,
                "https://eshop.parkland.co.kr/upload/tip_guide2/g_20241121105659536.jpg",
                true,
                UserStatus.NORMAL,
                UserRole.USER
        );
        User savedUser2 = userRepository.save(user2);

        Point point = new Point(BigDecimal.valueOf(10000), user1);
        pointRepository.save(point);

        Address address = new Address(savedUser1, "충청북도 청주시 서원구 월평로 24", 36.6, 127.5);
        addressRepository.save(address);

        Membership membership = new Membership(MembershipType.PREMIUM, LocalDateTime.now().plusYears(1));
        membershipRepository.save(membership);

        Banner banner = new Banner("ad", "ad", LocalDateTime.now().plusYears(1));
        bannerRepository.save(banner);

        Category category = new Category("category", "");
        Category savedCategory = categoryRepository.save(category);

        SubCategory subCategory = new SubCategory("운동", "", savedCategory);
        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);

        Coupon coupon = new Coupon("coupon", "couponDescription", 100);
        Coupon savedCoupon = couponRepository.save(coupon);

        CouponUser couponUser = new CouponUser(LocalDateTime.now().plusMonths(1), CouponUserStatus.UNUSED, savedUser1, savedCoupon);
        couponUserRepository.save(couponUser);

        Schedule schedule = new Schedule(LocalDateTime.of(2025, 1, 5, 13, 0, 0), "운동", "3대 200 입니다", user1);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        ScheduleSubCategory scheduleSubCategory = new ScheduleSubCategory("헬스하자", savedSchedule, savedSubCategory);
        scheduleSubCategoryRepository.save(scheduleSubCategory);

        Matching matching = new Matching(MatchStatus.ACCEPTED, savedSchedule, user1, user2);
        Matching savedMatching = matchRepository.save(matching);

        Report report = new Report("잠수탐", savedMatching, user1, user2);
        reportRepository.save(report);
    }
}
