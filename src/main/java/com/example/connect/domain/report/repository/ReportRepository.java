package com.example.connect.domain.report.repository;

import com.example.connect.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    default Report findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }
}
