package com.example.connect.domain.payment.repository;

import com.example.connect.domain.payment.dto.PaymentGetResDto;
import com.example.connect.domain.payment.entity.Payment;
import com.example.connect.global.enums.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select p from Payment p where p.type = :type order by p.createdAt desc")
    Page<PaymentGetResDto> findByType(@Param("type") PaymentType type, Pageable pageable);

    @Query("select p from Payment p join fetch p.user u where u.id = :userId order by p.createdAt desc")
    Page<PaymentGetResDto> findByUserData(Long userId, Pageable pageable);

    @Query(value = "select date(created_at), sum(amount), sum(sum(amount)) over (order by date(created_at)) from Payment" +
            " where created_at between :from and :to " +
            " group by date(created_at) order by date(created_at)"
            , nativeQuery = true)
    List<Object[]> findAllByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
