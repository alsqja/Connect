package com.example.connect.global.aop;

import com.example.connect.domain.match.dto.MatchingResDto;
import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.match.repository.MatchingRepository;
import com.example.connect.domain.notify.service.NotifyService;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.enums.MatchStatus;
import com.example.connect.global.enums.NotifyType;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class SendNotifyAspect {

    private final NotifyService notifyService;
    private final MatchingRepository matchingRepository;

    @AfterReturning(
            value = "@annotation(com.example.connect.global.aop.annotation.SendNotify)",
            returning = "resDto"
    )
    @Transactional
    public void afterUpdateMatchingStatus(JoinPoint jp, Object resDto) {
        if (resDto instanceof MatchingResDto dto) {
            if (dto.getStatus() == MatchStatus.PENDING) {
                Matching matching = matchingRepository.findByIdWithUser(dto.getId())
                        .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
                User receiver = matching.getToSchedule().getUser();
                notifyService.sendNotify(
                        receiver,
                        NotifyType.MATCHING_REQUEST,
                        matching.getToSchedule().getDate() + " " + matching.getToSchedule().getTitle() + " 일정에 매칭 요청이 도착했습니다.",
                        "/schedule/" + matching.getToSchedule().getId()
                );
            } else if (dto.getStatus() == MatchStatus.ACCEPTED) {
                Matching matching = matchingRepository.findByIdOrElseThrow(dto.getId());
                User receiver = matching.getFromSchedule().getUser();
                notifyService.sendNotify(
                        receiver,
                        NotifyType.MATCHING_ACCEPT,
                        matching.getFromSchedule().getDate() + " " + matching.getFromSchedule().getTitle() + " 일정에 매칭 요청이 수락되었습니다.",
                        "/schedule/" + matching.getFromSchedule().getId()
                );
            }
        }
    }
}
