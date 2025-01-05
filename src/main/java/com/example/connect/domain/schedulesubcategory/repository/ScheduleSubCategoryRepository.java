package com.example.connect.domain.schedulesubcategory.repository;

import com.example.connect.domain.schedulesubcategory.entity.ScheduleSubCategory;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleSubCategoryRepository extends JpaRepository<ScheduleSubCategory, Long> {
    default ScheduleSubCategory findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }
}
