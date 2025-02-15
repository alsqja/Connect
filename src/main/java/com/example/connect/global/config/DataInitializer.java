//package com.example.connect.global.config;
//
//import com.example.connect.domain.category.entity.Category;
//import com.example.connect.domain.category.repository.CategoryRepository;
//import com.example.connect.domain.coupon.entity.Coupon;
//import com.example.connect.domain.coupon.repository.CouponRepository;
//import com.example.connect.domain.couponuser.entity.CouponUser;
//import com.example.connect.domain.couponuser.repository.CouponUserRepository;
//import com.example.connect.domain.match.entity.Matching;
//import com.example.connect.domain.match.repository.MatchingRepository;
//import com.example.connect.domain.membership.entity.Membership;
//import com.example.connect.domain.membership.repository.MembershipRepository;
//import com.example.connect.domain.payment.entity.Payment;
//import com.example.connect.domain.payment.repository.PaymentRepository;
//import com.example.connect.domain.point.entity.Point;
//import com.example.connect.domain.point.repository.PointRepository;
//import com.example.connect.domain.report.entity.Report;
//import com.example.connect.domain.report.repository.ReportRepository;
//import com.example.connect.domain.schedule.entity.Schedule;
//import com.example.connect.domain.schedule.repository.ScheduleRepository;
//import com.example.connect.domain.schedulesubcategory.entity.ScheduleSubCategory;
//import com.example.connect.domain.schedulesubcategory.repository.ScheduleSubCategoryRepository;
//import com.example.connect.domain.subcategory.entity.SubCategory;
//import com.example.connect.domain.subcategory.repository.SubCategoryRepository;
//import com.example.connect.domain.user.entity.User;
//import com.example.connect.domain.user.repository.UserRepository;
//import com.example.connect.global.enums.CouponUserStatus;
//import com.example.connect.global.enums.Gender;
//import com.example.connect.global.enums.MatchStatus;
//import com.example.connect.global.enums.MembershipType;
//import com.example.connect.global.enums.PaymentStatus;
//import com.example.connect.global.enums.PaymentType;
//import com.example.connect.global.enums.UserRole;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Component
//@RequiredArgsConstructor
//@Profile("dev")
//public class DataInitializer {
//
//    private final UserRepository userRepository;
//    private final CategoryRepository categoryRepository;
//    private final CouponRepository couponRepository;
//    private final CouponUserRepository couponUserRepository;
//    private final MatchingRepository matchingRepository;
//    private final MembershipRepository membershipRepository;
//    private final PointRepository pointRepository;
//    private final ReportRepository reportRepository;
//    private final ScheduleRepository scheduleRepository;
//    private final ScheduleSubCategoryRepository scheduleSubCategoryRepository;
//    private final SubCategoryRepository subCategoryRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final PaymentRepository paymentRepository;
//
//    @PostConstruct
//    public void init() {
//        User user1 = new User(
//                "email@email.com",
//                passwordEncoder.encode("Password1!"),
//                "eunyoung",
//                "19970714",
//                Gender.WOMAN,
//                "https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512",
//                true,
//                UserRole.USER
//        );
//        User savedUser1 = userRepository.save(user1);
//
//        User user2 = new User(
//                "email2@email.com",
//                passwordEncoder.encode("Password1!"),
//                "minbeom",
//                "19970814",
//                Gender.MAN,
//                "https://eshop.parkland.co.kr/upload/tip_guide2/g_20241121105659536.jpg",
//                true,
//                UserRole.USER
//        );
//        User savedUser2 = userRepository.save(user2);
//
//        for (int i = 0; i < 100; i++) {
//            Gender gender = (i % 2 == 0) ? Gender.MAN : Gender.WOMAN;
//            UserRole role = i % 3 == 0 ? UserRole.ADMIN : UserRole.USER;
//            User user = new User("test@email.com" + i, passwordEncoder.encode("Password1!"), "name" + i, "19970814", gender, "https://eshop.parkland.co.kr/upload/tip_guide2/g_20241121105659536.jpg", i % 2 == 0, role);
//            userRepository.save(user);
//        }
//
//        Payment payment1 = new Payment(
//                "pay-1234",
//                "portone-1234",
//                BigDecimal.valueOf(1000),
//                PaymentType.POINT,
//                PaymentStatus.PAID,
//                "포인트 1000 결제",
//                user1
//        );
//        Payment savePayment1 = paymentRepository.save(payment1);
//
//        Point point = new Point(BigDecimal.valueOf(10000), user1, savePayment1, false);
//        pointRepository.save(point);
//
//        Membership membership = new Membership(MembershipType.PREMIUM, LocalDate.now().plusYears(1), user1, payment1);
//        membershipRepository.save(membership);
//
//        Category category = new Category("스포츠", "");
//        Category savedCategory = categoryRepository.save(category);
//
//        SubCategory subCategory = new SubCategory("축구", "", savedCategory);
//        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
//
//        Coupon coupon = new Coupon("coupon", "couponDescription", 100, 5, LocalDate.of(2025, 2, 28), LocalDateTime.of(2025, 1, 21, 0, 0, 0));
//        Coupon savedCoupon = couponRepository.save(coupon);
//
//        Coupon coupon1 = new Coupon("가입 축하 쿠폰", "couponDescription", 100, 5, LocalDate.of(2025, 2, 28), LocalDateTime.of(2025, 1, 21, 0, 0, 0));
//        Coupon savedCoupon1 = couponRepository.save(coupon1);
//
//        CouponUser couponUser = new CouponUser(LocalDate.now().plusMonths(1), CouponUserStatus.UNUSED, savedUser1, savedCoupon);
//        couponUserRepository.save(couponUser);
//
//        Schedule schedule1 = new Schedule(LocalDate.of(2025, 1, 30), "운동", "3대 200 입니다", "충청북도 청주시 서원구 월평로 24", 36.6, 127.5, user1);
//        Schedule savedSchedule1 = scheduleRepository.save(schedule1);
//
//        Schedule schedule2 = new Schedule(LocalDate.of(2025, 1, 30), "운동", "3대 200 입니다", "충청북도 청주시 서원구 월평로 24", 36.6, 127.5, user1);
//        Schedule savedSchedule2 = scheduleRepository.save(schedule2);
//
//        ScheduleSubCategory scheduleSubCategory = new ScheduleSubCategory("헬스하자", savedSchedule1, savedSubCategory);
//        scheduleSubCategoryRepository.save(scheduleSubCategory);
//
//        Matching matching = new Matching(MatchStatus.ACCEPTED, savedSchedule1, savedSchedule2, 1);
//        Matching savedMatching = matchingRepository.save(matching);
//
//        Report report = new Report("잠수탐", savedMatching, user1, user2);
//        reportRepository.save(report);
//
//        user2.addReportedCount();
//        userRepository.save(user2);
//    }
//
/// // /    @PostConstruct
/// // /    void initQueryTestData() {
/// // /
/// // /        for (int i = 0; i < 10000; i++) {
/// // /            Category category = new Category("name" + i, "");
/// // /            categoryRepository.save(category);
/// // /
/// // /            for (int j = 0; j < 10; j++) {
/// // /                SubCategory subCategory = new SubCategory(i + "subCate" + j, "", category);
/// // /                subCategoryRepository.save(subCategory);
/// // /            }
/// // /        }
/// // /
/// // /        for (int i = 0; i < 50000; i++) {
/// // /            UserRole role = UserRole.USER;
/// // /            User user1 = new User("email" + i + "@email.com", passwordEncoder.encode("Password1!"), "name" + i, "19970814", Gender.WOMAN, "https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512", true, role);
/// // /            userRepository.save(user1);
/// // /            User user2 = new User("2email" + i + "@email.com", passwordEncoder.encode("Password1!"), "2name" + i, "19970814", Gender.MAN, "https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512", true, role);
/// // /            userRepository.save(user2);
/// // /
/// // /            Schedule schedule1 = new Schedule(LocalDate.of(2025, 1, 30), "schedule" + i, "detail" + i, "address" + i, 35.9585739544886, 126.925554591431, user1);
/// // /            scheduleRepository.save(schedule1);
/// // /            Schedule schedule2 = new Schedule(LocalDate.of(2025, 1, 30), "2schedule" + i, "2detail" + i, "2address" + i, 35.9585739544886, 126.925554591431, user2);
/// // /            scheduleRepository.save(schedule2);
/// // /
/// // /            Matching matching = new Matching(MatchStatus.PENDING, schedule1, schedule2, 1);
/// // /            matchingRepository.save(matching);
/// // /
/// // /            SubCategory subCategory = subCategoryRepository.findByIdOrElseThrow((long) i + 1);
/// // /            ScheduleSubCategory scheduleSubCategory1 = new ScheduleSubCategory("description" + i, schedule1, subCategory);
/// // /            ScheduleSubCategory scheduleSubCategory2 = new ScheduleSubCategory("description" + i, schedule2, subCategory);
/// // /            scheduleSubCategoryRepository.save(scheduleSubCategory1);
/// // /            scheduleSubCategoryRepository.save(scheduleSubCategory2);
/// // /        }
/// // /    }
//}
