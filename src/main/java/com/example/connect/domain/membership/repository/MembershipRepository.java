package com.example.connect.domain.membership.repository;

import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    default Membership findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    Optional<Membership> findByUserIdAndExpiredDateAfter(Long id, LocalDate now);

    List<Membership> findByExpiredDateBefore(LocalDate now);

    Membership findByUserIdAndIsActiveTrue(Long userId);

    List<Membership> findByUserId(Long userId);

    @Query("select m.isActive from Membership m where m.user.id = :userId")
    List<Boolean> findIsActiveByUserId(Long userId);
}
