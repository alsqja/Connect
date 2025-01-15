package com.example.connect.domain.report.repository;

import com.example.connect.domain.report.entity.Report;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long> {
    default Report findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    int countAllByToUser(User toUser);

    @Query("select r from Report as r where r.fromUser.id = :userId")
    Page<Report> findByUserId(Long userId, Pageable pageable);

    Page<Report> findAllByToUserId(Long toUserId, Pageable pageable);
}
