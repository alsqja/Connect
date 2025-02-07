package com.example.connect.domain.schedule.repository;

import com.example.connect.domain.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CustomScheduleRepository {

    Page<Schedule> findAllPageSchedule(Pageable pageable, LocalDate startDate, LocalDate endDate, Long userId);
}
