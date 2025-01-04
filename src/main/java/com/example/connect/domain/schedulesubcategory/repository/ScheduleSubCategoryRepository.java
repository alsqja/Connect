package com.example.connect.domain.schedulesubcategory.repository;

import com.example.connect.domain.schedulesubcategory.entity.ScheduleSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleSubCategoryRepository extends JpaRepository<ScheduleSubCategory, Long> {
    default ScheduleSubCategory findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }
}
