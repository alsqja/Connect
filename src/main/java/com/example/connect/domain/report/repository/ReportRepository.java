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

    @Query("select r from Report as r join fetch r.toUser join fetch r.fromUser join fetch r.matching m join fetch m.fromSchedule join fetch m.toSchedule ts join fetch ts.user where r.fromUser.id = :userId")
    Page<Report> findByFromUserId(Long userId, Pageable pageable);

    @Query("select r from Report as r join fetch r.toUser join fetch r. fromUser join fetch r.matching m join fetch m.fromSchedule join fetch m.toSchedule ts join fetch  ts.user where r.toUser.id = :userId")
    Page<Report> findAllByToUserId(Long toUserId, Pageable pageable);
}
