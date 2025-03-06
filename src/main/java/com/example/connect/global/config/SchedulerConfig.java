package com.example.connect.global.config;

import com.example.connect.domain.coupon.service.CouponService;
import com.example.connect.domain.match.repository.MatchingRepository;
import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.domain.membership.repository.MembershipRepository;
import com.example.connect.domain.membership.service.MembershipService;
import com.example.connect.domain.notify.repository.NotifyRepository;
import com.example.connect.domain.point.service.PointService;
import com.example.connect.domain.pointuse.entity.PointUse;
import com.example.connect.domain.pointuse.repository.PointUseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SchedulerConfig {
    private final PointUseRepository pointUseRepository;
    private final PointService pointService;
    private final CouponService couponService;
    private final MatchingRepository matchingRepository;
    private final NotifyRepository notifyRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipService membershipService;

    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiration() {
        LocalDateTime now = LocalDateTime.now().minusYears(1);
        List<PointUse> pointUseList = pointUseRepository.findExpiredPoints(now);

        pointService.expiredPoint(pointUseList);
        couponService.expireCoupon();
        couponService.createBirthCouponUser();
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void autoPaymentMembership() {
        List<Membership> memberships = membershipRepository.findByExpiredDateBefore(LocalDate.now());

        for (Membership membership : memberships) {
            membershipService.autoPaymentMemberships(membership);
        }
    }

    @Scheduled(cron = "0 0 0 1 1 *")
    public void creteBirthCoupon() {
        couponService.createBirthCoupon();
    }

//    @Scheduled(cron = "0 0 6 * * *")
//    public void sendReviewReminder() {
//
//        List<Matching> matchings = matchingRepository.findYesterdayMatching(LocalDate.now().minusDays(1));
//
//        // 매칭을 기준으로 Notify 객체 생성
//        List<Notify> notifies = matchings.stream()
//                .flatMap(matching -> {
//                    User toUser = matching.getToSchedule().getUser();
//                    User fromUser = matching.getFromSchedule().getUser();
//                    Long matchingId = matching.getId(); // 매칭 ID 가져오기
//
//                    Notify notifyTo = new Notify(NotifyType.REVIEW, fromUser.getName() + "님과 매칭에 대해 리뷰를 남겨주세요.", toUser, "matching/" + matchingId + "/user/" + fromUser.getId());
//                    Notify notifyFrom = new Notify(NotifyType.REVIEW, toUser.getName() + "님과 매칭에 대해 리뷰를 남겨주세요.", fromUser, "matching/" + matchingId + "/user/" + toUser.getId());
//
//                    return Stream.of(notifyTo, notifyFrom);
//                })
//                .toList();
//
//        notifyRepository.saveAll(notifies);
//    }
}
