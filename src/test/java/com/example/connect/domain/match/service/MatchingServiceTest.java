package com.example.connect.domain.match.service;

import com.example.connect.domain.category.entity.Category;
import com.example.connect.domain.category.repository.CategoryRepository;
import com.example.connect.domain.match.dto.MatchingResDto;
import com.example.connect.domain.match.repository.MatchingRepository;
import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.schedule.repository.ScheduleRepository;
import com.example.connect.domain.schedulesubcategory.entity.ScheduleSubCategory;
import com.example.connect.domain.schedulesubcategory.repository.ScheduleSubCategoryRepository;
import com.example.connect.domain.subcategory.entity.SubCategory;
import com.example.connect.domain.subcategory.repository.SubCategoryRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.UserRole;
import com.example.connect.global.error.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Profile("test")
class MatchingServiceTest {

    @Autowired
    MatchingService matchingService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    MatchingRepository matchingRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    SubCategoryRepository subCategoryRepository;
    @Autowired
    ScheduleSubCategoryRepository scheduleSubCategoryRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("100% 일치 시나리오")
    void createMatching1() {
        User user1 = new User(
                "email@email.com",
                passwordEncoder.encode("Password1!"),
                "eunyoung",
                "19970714",
                Gender.WOMAN,
                "https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512",
                true,
                UserRole.USER
        );
        User savedUser1 = userRepository.save(user1);

        User user2 = new User(
                "email2@email.com",
                passwordEncoder.encode("Password1!"),
                "minbeom",
                "19970814",
                Gender.MAN,
                "https://eshop.parkland.co.kr/upload/tip_guide2/g_20241121105659536.jpg",
                true,
                UserRole.USER
        );
        User savedUser2 = userRepository.save(user2);

        User user3 = new User(
                "email3@email.com",
                passwordEncoder.encode("Password1!"),
                "현지님",
                "19970105",
                Gender.WOMAN,
                "https://eshop.parkland.co.kr/upload/tip_guide2/g_20241121105659536.jpg",
                true,
                UserRole.USER
        );
        User savedUser3 = userRepository.save(user3);

        User user4 = new User(
                "email4@email.com",
                passwordEncoder.encode("Password1!"),
                "경섭님",
                "19970105",
                Gender.WOMAN,
                "https://eshop.parkland.co.kr/upload/tip_guide2/g_20241121105659536.jpg",
                true,
                UserRole.USER
        );
        User savedUser4 = userRepository.save(user4);

        Category category = new Category("스포츠", "");
        Category savedCategory = categoryRepository.save(category);
        Category category1 = new Category("공부", "");
        Category savedCategory1 = categoryRepository.save(category1);
        SubCategory subCategory1 = new SubCategory("축구", "", savedCategory);
        SubCategory savedSubCategory1 = subCategoryRepository.save(subCategory1);
        SubCategory subCategory2 = new SubCategory("야구", "", savedCategory);
        SubCategory savedSubCategory2 = subCategoryRepository.save(subCategory2);
        SubCategory subCategory3 = new SubCategory("볼링", "", savedCategory);
        SubCategory savedSubCategory3 = subCategoryRepository.save(subCategory3);
        SubCategory subCategory4 = new SubCategory("redis", "", savedCategory1);
        SubCategory savedSubCategory4 = subCategoryRepository.save(subCategory4);

        Schedule schedule1 = new Schedule(LocalDate.of(2025, 1, 30), "운동1", "3대 200 입니다", "충청북도 청주시 서원구 월평로 24", 37.557193, 127.079415, user1);
        Schedule savedSchedule1 = scheduleRepository.save(schedule1);

        Schedule schedule2 = new Schedule(LocalDate.of(2025, 1, 30), "운동2", "3대 500 입니다", "충청북도 청주시 서원구 월평로 24", 37.561949, 127.038485, user2);
        Schedule savedSchedule2 = scheduleRepository.save(schedule2);

        Schedule schedule3 = new Schedule(LocalDate.of(2025, 1, 30), "운동2", "3대 500 입니다", "충청북도 청주시 서원구 월평로 24", 37.561949, 127.038486, user3);
        Schedule savedSchedule3 = scheduleRepository.save(schedule3);

        Schedule schedule4 = new Schedule(LocalDate.of(2025, 1, 30), "공부", "3대 500 입니다", "충청북도 청주시 서원구 월평로 24", 37.561941, 127.038486, user4);
        Schedule savedSchedule4 = scheduleRepository.save(schedule4);

        ScheduleSubCategory scheduleSubCategory1 = new ScheduleSubCategory("헬스하자", savedSchedule1, savedSubCategory1);
        scheduleSubCategoryRepository.save(scheduleSubCategory1);
        ScheduleSubCategory scheduleSubCategory2 = new ScheduleSubCategory("헬스하자", savedSchedule1, savedSubCategory2);
        scheduleSubCategoryRepository.save(scheduleSubCategory2);
        ScheduleSubCategory scheduleSubCategory = new ScheduleSubCategory("헬스하자", savedSchedule2, savedSubCategory1);
        scheduleSubCategoryRepository.save(scheduleSubCategory);
//        ScheduleSubCategory scheduleSubCategory22 = new ScheduleSubCategory("헬스하자", savedSchedule2, savedSubCategory2);
//        scheduleSubCategoryRepository.save(scheduleSubCategory22);
        ScheduleSubCategory scheduleSubCategory3 = new ScheduleSubCategory("헬스하자", savedSchedule3, savedSubCategory3);
        scheduleSubCategoryRepository.save(scheduleSubCategory3);
        ScheduleSubCategory scheduleSubCategory4 = new ScheduleSubCategory("헬스하자", savedSchedule4, savedSubCategory4);
        scheduleSubCategoryRepository.save(scheduleSubCategory4);
//        ScheduleSubCategory scheduleSubCategory5 = new ScheduleSubCategory("공부하자 은영아", savedSchedule1, savedSubCategory4);
//        scheduleSubCategoryRepository.save(scheduleSubCategory5);
//        ScheduleSubCategory scheduleSubCategory6 = new ScheduleSubCategory("공부하자 민범아", savedSchedule2, savedSubCategory4);
//        scheduleSubCategoryRepository.save(scheduleSubCategory6);
//        ScheduleSubCategory scheduleSubCategory7 = new ScheduleSubCategory("공부하자 은영아", savedSchedule2, savedSubCategory3);
//        scheduleSubCategoryRepository.save(scheduleSubCategory7);

        entityManager.flush();
        entityManager.clear();

        MatchingResDto result1 = matchingService.createMatching(user1.getId(), savedSchedule1.getId());
        MatchingResDto result2 = matchingService.createMatching(user2.getId(), savedSchedule2.getId());
//        MatchingResDto result3 = matchingService.createMatching(user3.getId(), savedSchedule3.getId());
//        MatchingResDto result4 = matchingService.createMatching(user4.getId(), savedSchedule4.getId());

        System.out.println(result1.getSimilarity());
        System.out.println(result2.getSimilarity());
//        System.out.println(result3.getSimilarity());
//        System.out.println(result4.getSimilarity());

        assertTrue(result1.getSimilarity() < 1, "similarity: " + result1.getSimilarity());
        assertTrue(result2.getSimilarity() < 1, "similarity: " + result2.getSimilarity());
//        assertEquals(0.3, result3.getSimilarity(), "similarity: " + result3.getSimilarity());
//        assertEquals(0.0, result4.getSimilarity(), "similarity: " + result4.getSimilarity());
    }

    @Test
    @DisplayName("1 중 1 일치")
    void createMatchingTest2() {
        User user1 = new User(
                "email@email.com",
                passwordEncoder.encode("Password1!"),
                "eunyoung",
                "19970714",
                Gender.WOMAN,
                "https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512",
                true,
                UserRole.USER
        );
        User savedUser1 = userRepository.save(user1);

        User user2 = new User(
                "email2@email.com",
                passwordEncoder.encode("Password1!"),
                "minbeom",
                "19970814",
                Gender.MAN,
                "https://eshop.parkland.co.kr/upload/tip_guide2/g_20241121105659536.jpg",
                true,
                UserRole.USER
        );
        User savedUser2 = userRepository.save(user2);

        Category category = new Category("스포츠", "");
        Category savedCategory = categoryRepository.save(category);
        SubCategory subCategory1 = new SubCategory("축구", "", savedCategory);
        SubCategory savedSubCategory1 = subCategoryRepository.save(subCategory1);

        Schedule schedule1 = new Schedule(LocalDate.of(2025, 1, 30), "운동1", "3대 200 입니다", "충청북도 청주시 서원구 월평로 24", 37.557193, 127.079415, user1);
        Schedule savedSchedule1 = scheduleRepository.save(schedule1);

        Schedule schedule2 = new Schedule(LocalDate.of(2025, 1, 30), "운동2", "3대 500 입니다", "충청북도 청주시 서원구 월평로 24", 37.561949, 127.038485, user2);
        Schedule savedSchedule2 = scheduleRepository.save(schedule2);

        ScheduleSubCategory scheduleSubCategory1 = new ScheduleSubCategory("헬스하자", savedSchedule1, savedSubCategory1);
        scheduleSubCategoryRepository.save(scheduleSubCategory1);
        ScheduleSubCategory scheduleSubCategory2 = new ScheduleSubCategory("헬스하자", savedSchedule2, savedSubCategory1);
        scheduleSubCategoryRepository.save(scheduleSubCategory2);

        entityManager.flush();
        entityManager.clear();

        MatchingResDto result1 = matchingService.createMatching(user1.getId(), savedSchedule1.getId());
        MatchingResDto result2 = matchingService.createMatching(user2.getId(), savedSchedule2.getId());

        System.out.println(result1.getSimilarity());
        System.out.println(result2.getSimilarity());

        assertEquals(1, (double) result1.getSimilarity(), "similarity: " + result1.getSimilarity());
        assertEquals(1, (double) result2.getSimilarity(), "similarity: " + result2.getSimilarity());
    }

    @Test
    @DisplayName("1 중 1 카테고리")
    public void createMatchingTest2_1() {
        User user1 = new User(
                "email@email.com",
                passwordEncoder.encode("Password1!"),
                "eunyoung",
                "19970714",
                Gender.WOMAN,
                "https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512",
                true,
                UserRole.USER
        );
        User savedUser1 = userRepository.save(user1);

        User user2 = new User(
                "email2@email.com",
                passwordEncoder.encode("Password1!"),
                "minbeom",
                "19970814",
                Gender.MAN,
                "https://eshop.parkland.co.kr/upload/tip_guide2/g_20241121105659536.jpg",
                true,
                UserRole.USER
        );
        User savedUser2 = userRepository.save(user2);

        Category category = new Category("스포츠", "");
        Category savedCategory = categoryRepository.save(category);
        SubCategory subCategory1 = new SubCategory("축구", "", savedCategory);
        SubCategory savedSubCategory1 = subCategoryRepository.save(subCategory1);
        SubCategory subCategory2 = new SubCategory("야구", "", savedCategory);
        SubCategory savedSubCategory2 = subCategoryRepository.save(subCategory2);

        Schedule schedule1 = new Schedule(LocalDate.of(2025, 1, 30), "운동1", "3대 200 입니다", "충청북도 청주시 서원구 월평로 24", 37.557193, 127.079415, user1);
        Schedule savedSchedule1 = scheduleRepository.save(schedule1);

        Schedule schedule2 = new Schedule(LocalDate.of(2025, 1, 30), "운동2", "3대 500 입니다", "충청북도 청주시 서원구 월평로 24", 37.561949, 127.038485, user2);
        Schedule savedSchedule2 = scheduleRepository.save(schedule2);

        ScheduleSubCategory scheduleSubCategory1 = new ScheduleSubCategory("헬스하자", savedSchedule1, savedSubCategory1);
        scheduleSubCategoryRepository.save(scheduleSubCategory1);
        ScheduleSubCategory scheduleSubCategory2 = new ScheduleSubCategory("헬스하자", savedSchedule2, savedSubCategory2);
        scheduleSubCategoryRepository.save(scheduleSubCategory2);

        entityManager.flush();
        entityManager.clear();

        MatchingResDto result1 = matchingService.createMatching(user1.getId(), savedSchedule1.getId());
        MatchingResDto result2 = matchingService.createMatching(user2.getId(), savedSchedule2.getId());

        System.out.println(result1.getSimilarity());
        System.out.println(result2.getSimilarity());

        assertTrue(result1.getSimilarity() < 1, "similarity: " + result1.getSimilarity());
        assertTrue(result2.getSimilarity() < 1, "similarity: " + result2.getSimilarity());
        assertEquals(result1.getSimilarity(), result2.getSimilarity());
    }

    @Test
    @DisplayName("2 중 1 일치")
    void createMatchingTest3() {
        User user1 = new User(
                "email@email.com",
                passwordEncoder.encode("Password1!"),
                "eunyoung",
                "19970714",
                Gender.WOMAN,
                "https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512",
                true,
                UserRole.USER
        );
        User savedUser1 = userRepository.save(user1);

        User user2 = new User(
                "email2@email.com",
                passwordEncoder.encode("Password1!"),
                "minbeom",
                "19970814",
                Gender.MAN,
                "https://eshop.parkland.co.kr/upload/tip_guide2/g_20241121105659536.jpg",
                true,
                UserRole.USER
        );
        User savedUser2 = userRepository.save(user2);

        Category category = new Category("스포츠", "");
        Category savedCategory = categoryRepository.save(category);
        SubCategory subCategory1 = new SubCategory("축구", "", savedCategory);
        SubCategory savedSubCategory1 = subCategoryRepository.save(subCategory1);
        SubCategory subCategory2 = new SubCategory("야구", "", savedCategory);
        SubCategory savedSubCategory2 = subCategoryRepository.save(subCategory2);
        Category category1 = new Category("공부", "");
        Category savedCategory1 = categoryRepository.save(category1);
        SubCategory subCategory3 = new SubCategory("redis", "", savedCategory1);
        SubCategory savedSubCategory3 = subCategoryRepository.save(subCategory3);

        Schedule schedule1 = new Schedule(LocalDate.of(2025, 1, 30), "운동1", "3대 200 입니다", "충청북도 청주시 서원구 월평로 24", 37.557193, 127.079415, user1);
        Schedule savedSchedule1 = scheduleRepository.save(schedule1);

        Schedule schedule2 = new Schedule(LocalDate.of(2025, 1, 30), "운동2", "3대 500 입니다", "충청북도 청주시 서원구 월평로 24", 37.561949, 127.038485, user2);
        Schedule savedSchedule2 = scheduleRepository.save(schedule2);

        ScheduleSubCategory scheduleSubCategory1 = new ScheduleSubCategory("헬스하자", savedSchedule1, savedSubCategory1);
        scheduleSubCategoryRepository.save(scheduleSubCategory1);
        ScheduleSubCategory scheduleSubCategory3 = new ScheduleSubCategory("헬스하자", savedSchedule1, savedSubCategory2);
        scheduleSubCategoryRepository.save(scheduleSubCategory3);
        ScheduleSubCategory scheduleSubCategory2 = new ScheduleSubCategory("헬스하자", savedSchedule2, savedSubCategory1);
        scheduleSubCategoryRepository.save(scheduleSubCategory2);
        ScheduleSubCategory scheduleSubCategory4 = new ScheduleSubCategory("헬스하자", savedSchedule2, savedSubCategory3);
        scheduleSubCategoryRepository.save(scheduleSubCategory4);

        entityManager.flush();
        entityManager.clear();

        MatchingResDto result1 = matchingService.createMatching(user1.getId(), savedSchedule1.getId());
        MatchingResDto result2 = matchingService.createMatching(user2.getId(), savedSchedule2.getId());

        System.out.println(result1.getSimilarity());
        System.out.println(result2.getSimilarity());

        assertTrue(result1.getSimilarity() < 1, "similarity: " + result1.getSimilarity());
        assertTrue(result2.getSimilarity() < 1, "similarity: " + result2.getSimilarity());
        assertEquals(result1.getSimilarity(), result2.getSimilarity());
    }

    @Test
    @DisplayName("2 중 1 일치 1 카테고리 일치")
    void createMatchingTest4() {
        User user1 = new User(
                "email@email.com",
                passwordEncoder.encode("Password1!"),
                "eunyoung",
                "19970714",
                Gender.WOMAN,
                "https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512",
                true,
                UserRole.USER
        );
        User savedUser1 = userRepository.save(user1);

        User user2 = new User(
                "email2@email.com",
                passwordEncoder.encode("Password1!"),
                "minbeom",
                "19970814",
                Gender.MAN,
                "https://eshop.parkland.co.kr/upload/tip_guide2/g_20241121105659536.jpg",
                true,
                UserRole.USER
        );
        User savedUser2 = userRepository.save(user2);

        Category category = new Category("스포츠", "");
        Category savedCategory = categoryRepository.save(category);
        SubCategory subCategory1 = new SubCategory("축구", "", savedCategory);
        SubCategory savedSubCategory1 = subCategoryRepository.save(subCategory1);
        SubCategory subCategory2 = new SubCategory("야구", "", savedCategory);
        SubCategory savedSubCategory2 = subCategoryRepository.save(subCategory2);
        SubCategory subCategory3 = new SubCategory("볼링", "", savedCategory);
        SubCategory savedSubCategory3 = subCategoryRepository.save(subCategory3);

        Schedule schedule1 = new Schedule(LocalDate.of(2025, 1, 30), "운동1", "3대 200 입니다", "충청북도 청주시 서원구 월평로 24", 37.557193, 127.079415, user1);
        Schedule savedSchedule1 = scheduleRepository.save(schedule1);

        Schedule schedule2 = new Schedule(LocalDate.of(2025, 1, 30), "운동2", "3대 500 입니다", "충청북도 청주시 서원구 월평로 24", 37.561949, 127.038485, user2);
        Schedule savedSchedule2 = scheduleRepository.save(schedule2);

        ScheduleSubCategory scheduleSubCategory1 = new ScheduleSubCategory("헬스하자", savedSchedule1, savedSubCategory1);
        scheduleSubCategoryRepository.save(scheduleSubCategory1);
        ScheduleSubCategory scheduleSubCategory3 = new ScheduleSubCategory("헬스하자", savedSchedule1, savedSubCategory2);
        scheduleSubCategoryRepository.save(scheduleSubCategory3);
        ScheduleSubCategory scheduleSubCategory2 = new ScheduleSubCategory("헬스하자", savedSchedule2, savedSubCategory1);
        scheduleSubCategoryRepository.save(scheduleSubCategory2);
        ScheduleSubCategory scheduleSubCategory4 = new ScheduleSubCategory("헬스하자", savedSchedule2, savedSubCategory3);
        scheduleSubCategoryRepository.save(scheduleSubCategory4);

        entityManager.flush();
        entityManager.clear();

        MatchingResDto result1 = matchingService.createMatching(user1.getId(), savedSchedule1.getId());
        MatchingResDto result2 = matchingService.createMatching(user2.getId(), savedSchedule2.getId());

        System.out.println(result1.getSimilarity());
        System.out.println(result2.getSimilarity());

        assertTrue(result1.getSimilarity() < 1, "similarity: " + result1.getSimilarity());
        assertTrue(result2.getSimilarity() < 1, "similarity: " + result2.getSimilarity());
        assertEquals(result1.getSimilarity(), result2.getSimilarity());
    }

    @Test
    @DisplayName("거리 10km 이상")
    void createMatchingTest_fail_distance() {
        User user1 = new User(
                "email@email.com",
                passwordEncoder.encode("Password1!"),
                "eunyoung",
                "19970714",
                Gender.WOMAN,
                "https://ca.slack-edge.com/T06B9PCLY1E-U07KRNHKXUM-4ddfb9e4780d-512",
                true,
                UserRole.USER
        );
        User savedUser1 = userRepository.save(user1);

        User user2 = new User(
                "email2@email.com",
                passwordEncoder.encode("Password1!"),
                "minbeom",
                "19970814",
                Gender.MAN,
                "https://eshop.parkland.co.kr/upload/tip_guide2/g_20241121105659536.jpg",
                true,
                UserRole.USER
        );
        User savedUser2 = userRepository.save(user2);

        Category category = new Category("스포츠", "");
        Category savedCategory = categoryRepository.save(category);
        SubCategory subCategory1 = new SubCategory("축구", "", savedCategory);
        SubCategory savedSubCategory1 = subCategoryRepository.save(subCategory1);

        Schedule schedule1 = new Schedule(LocalDate.of(2025, 1, 30), "운동1", "3대 200 입니다", "충청북도 청주시 서원구 월평로 24", 37.557193, 127.079415, user1);
        Schedule savedSchedule1 = scheduleRepository.save(schedule1);

        Schedule schedule2 = new Schedule(LocalDate.of(2025, 1, 30), "운동2", "3대 500 입니다", "충청북도 청주시 서원구 월평로 24", 33.5002607, 126.5209372, user2);
        Schedule savedSchedule2 = scheduleRepository.save(schedule2);

        ScheduleSubCategory scheduleSubCategory1 = new ScheduleSubCategory("헬스하자", savedSchedule1, savedSubCategory1);
        scheduleSubCategoryRepository.save(scheduleSubCategory1);
        ScheduleSubCategory scheduleSubCategory2 = new ScheduleSubCategory("헬스하자", savedSchedule2, savedSubCategory1);
        scheduleSubCategoryRepository.save(scheduleSubCategory2);

        entityManager.flush();
        entityManager.clear();

        NotFoundException e1 = assertThrows(NotFoundException.class, () -> matchingService.createMatching(user2.getId(), savedSchedule2.getId()));
        NotFoundException e2 = assertThrows(NotFoundException.class, () -> matchingService.createMatching(user1.getId(), savedSchedule1.getId()));
    }
}