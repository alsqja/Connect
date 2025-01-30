package com.example.connect.domain.payment.service;

import com.example.connect.domain.payment.dto.AutoPaymentReqDto;
import com.example.connect.domain.payment.dto.AutoPaymentResDto;
import com.example.connect.domain.payment.dto.PaymentListResDto;
import com.example.connect.domain.payment.dto.PaymentReqDto;
import com.example.connect.domain.payment.dto.PaymentResDto;
import com.example.connect.domain.payment.entity.Payment;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.enums.UserRole;

import java.math.BigDecimal;

public interface PaymentService {
    Payment createPaymentCheck(PaymentReqDto paymentReqDto, User user);

    PaymentResDto cancelPayment(Long paymentId, BigDecimal amount, String reason);

    PaymentListResDto getAllPayments(Long userId, UserRole userRole, String type, int page, int size);

    AutoPaymentResDto cardPayment(String paymentId, AutoPaymentReqDto autoPaymentReqDto);

    void cancelPaymentPortone(String paymentUid, String reason);
}
