package com.example.connect.domain.membership.repository;

import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    default Membership findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    Optional<Membership> findByUserIdAndExpiredDateAfter(Long id, LocalDate now);

    List<Membership> findByExpiredDateBeforeAndIsActiveTrue(LocalDate now);
}
