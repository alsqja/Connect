package com.example.connect.domain.schedule.repository;

import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    default Schedule findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    boolean existsScheduleByUserAndDate(User user, LocalDate date);
}
