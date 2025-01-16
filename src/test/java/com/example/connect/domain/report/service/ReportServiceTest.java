package com.example.connect.domain.report.service;

import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.match.repository.MatchingRepository;
import com.example.connect.domain.report.dto.ReportResDto;
import com.example.connect.domain.report.entity.Report;
import com.example.connect.domain.report.repository.ReportRepository;
import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.schedule.repository.ScheduleRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.MatchStatus;
import com.example.connect.global.enums.UserRole;
import com.example.connect.global.enums.UserStatus;
import com.example.connect.global.error.exception.BadRequestException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Profile("test")
class ReportServiceTest {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MatchingRepository matchingRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ReportService reportService;

    User user1;
    User user2;
    Schedule schedule1;
    Schedule schedule2;
    Matching matching;

    @BeforeEach
    void init() {
        user1 = new User(
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

        user2 = new User(
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

        schedule1 = new Schedule(LocalDate.of(2025, 1, 30), "운동1", "3대 200 입니다", "충청북도 청주시 서원구 월평로 24", 37.557193, 127.079415, user1);
        Schedule savedSchedule1 = scheduleRepository.save(schedule1);

        schedule2 = new Schedule(LocalDate.of(2025, 1, 30), "운동2", "3대 500 입니다", "충청북도 청주시 서원구 월평로 24", 37.561949, 127.038485, user2);
        Schedule savedSchedule2 = scheduleRepository.save(schedule2);

        matching = new Matching(MatchStatus.CREATED, schedule1, schedule2, 1);
        Matching savedMatching = matchingRepository.save(matching);
    }

    @Test
    void createReport() {

        user2.addReportedCount();
        user2.addReportedCount();
        user2.addReportedCount();
        user2.addReportedCount();
        userRepository.save(user2);

        ReportResDto result = reportService.createReport(user1.getId(), user2.getId(), matching.getId(), "잠수탐");

        assertEquals(user1.getId(), result.getFromId());
        assertEquals(user2.getId(), result.getToId());
        assertEquals(matching.getId(), result.getMatchingId());
        assertEquals(5, user2.getReportedCount());
        assertEquals(UserStatus.REJECTED, user2.getStatus());

        BadRequestException sameUserException = assertThrows(BadRequestException.class, () -> reportService.createReport(user1.getId(), user1.getId(), matching.getId(), "잠수탐"));
        BadRequestException existReportException = assertThrows(BadRequestException.class, () -> reportService.createReport(user1.getId(), user2.getId(), matching.getId(), "잠수탐"));
    }
}