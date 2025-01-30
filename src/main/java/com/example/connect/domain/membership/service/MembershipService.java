package com.example.connect.domain.membership.service;

import com.example.connect.domain.card.dto.CardReqDto;
import com.example.connect.domain.card.repository.CardRepository;
import com.example.connect.domain.card.service.CardService;
import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.domain.membership.repository.MembershipRepository;
import com.example.connect.domain.payment.dto.AutoPaymentReqDto;
import com.example.connect.domain.payment.dto.AutoPaymentResDto;
import com.example.connect.domain.payment.dto.EncryptReqDto;
import com.example.connect.domain.payment.dto.PaidPaymentAmountDto;
import com.example.connect.domain.payment.dto.PaymentAutomaticReqDto;
import com.example.connect.domain.payment.dto.PaymentReqDto;
import com.example.connect.domain.payment.dto.PaymentResDto;
import com.example.connect.domain.payment.entity.Payment;
import com.example.connect.domain.payment.repository.PaymentRepository;
import com.example.connect.domain.payment.service.PaymentService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.RedisTokenRepository;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.enums.MembershipType;
import com.example.connect.global.enums.PaymentStatus;
import com.example.connect.global.enums.PaymentType;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import com.example.connect.global.util.AES256Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final RedisTokenRepository redisTokenRepository;
    private final CardRepository cardRepository;
    private final CardService cardService;
    private final AES256Util aes256Util;

    @Value("${portone.api.channel.key}")
    private String channelKey;

    /**
     * 결제 추가 (membership)
     */
    @Transactional
    public PaymentResDto createPaymentMembership(
            EncryptReqDto encryptReqDto,
            Long userId
    ) {
        String decodingString = aes256Util.decryptAES(encryptReqDto.getEncryptData());
        PaymentAutomaticReqDto paymentAutomaticReqDto = aes256Util.objectMapping(decodingString, PaymentAutomaticReqDto.class);

        User user = userRepository.findByIdOrElseThrow(userId);
        LocalDate expiredDate = LocalDate.now().plusMonths(1);
        MembershipType membershipType;

        if (Objects.equals(paymentAutomaticReqDto.getAmount(), BigDecimal.valueOf(4900))) {
            membershipType = MembershipType.NORMAL;
        } else if (Objects.equals(paymentAutomaticReqDto.getAmount(), BigDecimal.valueOf(9900))) {
            membershipType = MembershipType.PREMIUM;
        } else {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        CardReqDto cardReqDto = new CardReqDto(
                paymentAutomaticReqDto.getCardNumber(),
                paymentAutomaticReqDto.getExpiredYear(),
                paymentAutomaticReqDto.getExpiredMonth(),
                paymentAutomaticReqDto.getCardPassword(),
                paymentAutomaticReqDto.getBirth()
        );

        AutoPaymentReqDto autoPaymentReqDto = new AutoPaymentReqDto(
                channelKey,
                paymentAutomaticReqDto.getDetails(),
                new PaidPaymentAmountDto(paymentAutomaticReqDto.getAmount().intValue()),
                "KRW",
                new AutoPaymentReqDto.Method(
                        new AutoPaymentReqDto.CardData(
                                cardReqDto
                        )
                )
        );

        AutoPaymentResDto resDto = paymentService.cardPayment(paymentAutomaticReqDto.getPayUid(), autoPaymentReqDto);

        try {
            PaymentReqDto paymentReqDto = new PaymentReqDto(
                    paymentAutomaticReqDto.getPayUid(),
                    resDto.getPayment().getPgTxId(),
                    paymentAutomaticReqDto.getAmount(),
                    paymentAutomaticReqDto.getDetails(),
                    paymentAutomaticReqDto.getType(),
                    paymentAutomaticReqDto.getStatus()
            );

            Payment payment = paymentService.createPaymentCheck(paymentReqDto, user);
            paymentRepository.save(payment);

            Membership membership = new Membership(
                    membershipType,
                    expiredDate,
                    user,
                    payment
            );

            membershipRepository.save(membership);
            cardService.createCard(cardReqDto, membership);

            RedisUserDto redisUserDto = new RedisUserDto(user);

            redisUserDto.updateMembership(membershipType, expiredDate);
            redisTokenRepository.saveUser(redisUserDto);

            PaymentResDto paymentResDto = new PaymentResDto(
                    payment.getPayUid(),
                    payment.getPortoneUid(),
                    payment.getAmount(),
                    payment.getDetails(),
                    payment.getType(),
                    payment.getPayStatus()
            );

            return paymentResDto;
        } catch (Exception e) {
            paymentService.cancelPaymentPortone(paymentAutomaticReqDto.getPayUid(), "내부 결제 오류로인한 결제 취소");

            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }
    }

    /**
     * 스케줄 자동 결제
     */
    @Transactional
    public void autoPaymentMemberships(Membership membership) {
        try {
            CardReqDto card = cardService.decryptCard(cardRepository.findByMembershipId(membership.getId()));
            String paymentUid = "pay-" + UUID.randomUUID().toString().substring(0, 16);
            String month = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
            int amount;

            if (membership.getType().equals(MembershipType.PREMIUM)) {
                amount = 9900;
            } else {
                amount = 4900;
            }

            AutoPaymentReqDto autoPaymentReqDto = new AutoPaymentReqDto(
                    channelKey,
                    month + "월 구독 결제 ( " + amount + "원 결제 )",
                    new PaidPaymentAmountDto(amount),
                    "KRW",
                    new AutoPaymentReqDto.Method(
                            new AutoPaymentReqDto.CardData(card)
                    )
            );

            AutoPaymentResDto autoPaymentResDto = paymentService.cardPayment(paymentUid, autoPaymentReqDto);

            Payment payment = new Payment(
                    paymentUid,
                    autoPaymentResDto.getPayment().getPgTxId(),
                    BigDecimal.valueOf(amount),
                    PaymentType.SUBSCRIBE,
                    PaymentStatus.PAID,
                    month + "월 구독 결제 ( " + amount + "원 결제 )",
                    membership.getUser()
            );

            paymentRepository.save(payment);
            membership.update(LocalDate.now().plusMonths(1), payment);
            membershipRepository.save(membership);
        } catch (Exception e) {
            membershipRepository.delete(membership);
        }
    }
}
