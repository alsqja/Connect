package com.example.connect.domain.schedule.repository;

import com.example.connect.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    default Schedule findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }
}
