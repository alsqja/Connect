package com.example.connect.domain.payment.repository;

import com.example.connect.domain.payment.dto.PaymentGetResDto;
import com.example.connect.domain.payment.entity.Payment;
import com.example.connect.global.enums.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select p from Payment p where p.type = :type")
    Page<PaymentGetResDto> findByType(@Param("type") PaymentType type, Pageable pageable);

    Page<PaymentGetResDto> findByUserId(Long userId, Pageable pageable);
}
