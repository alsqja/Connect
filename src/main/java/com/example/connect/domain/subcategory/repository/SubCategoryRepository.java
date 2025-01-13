package com.example.connect.domain.subcategory.repository;

import com.example.connect.domain.schedulesubcategory.dto.ScheduleSubCategoryResDto;
import com.example.connect.domain.subcategory.entity.SubCategory;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    default SubCategory findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query("""
            SELECT new com.example.connect.domain.schedulesubcategory.dto.ScheduleSubCategoryResDto(
                ssc.id,
                sc.id,
                sc.name,
                ssc.description,
                sc.imageUrl
            ) FROM ScheduleSubCategory ssc
            LEFT JOIN ssc.subCategory sc
            LEFT JOIN ssc.schedule s
            WHERE s.id = :id
            """)
    List<ScheduleSubCategoryResDto> findDetailsByScheduleId(Long id);
}
