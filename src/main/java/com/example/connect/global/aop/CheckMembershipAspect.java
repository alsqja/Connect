package com.example.connect.global.aop;

import com.example.connect.domain.match.dto.MatchingWithScheduleResDto;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.user.dto.UserSimpleResDto;
import com.example.connect.global.common.Const;
import com.example.connect.global.config.auth.UserDetailsImpl;
import com.example.connect.global.enums.MembershipType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class CheckMembershipAspect {

    @Around(value = "@annotation(com.example.connect.global.aop.annotation.CheckMembership) && args(id)",
            argNames = "joinPoint,id")
    public Object checkUserMembership(ProceedingJoinPoint joinPoint, Long id) throws Throwable {

        UserSimpleResDto result = (UserSimpleResDto) joinPoint.proceed();

        RedisUserDto me = getCurrentUser();

        if (id.equals(me.getId())) {
            return result;
        }

        if (!MembershipType.PREMIUM.equals(me.getMembershipType())) {
            return maskUserSimpleResDto(result);
        }

        return result;
    }

    @Around(value = "@annotation(com.example.connect.global.aop.annotation.CheckMembership) && args(userId, scheduleId)",
            argNames = "joinPoint,userId,scheduleId")
    public Object checkMatchingMembership(ProceedingJoinPoint joinPoint, Long userId, Long scheduleId) throws Throwable {

        MatchingWithScheduleResDto result = (MatchingWithScheduleResDto) joinPoint.proceed();

        if (!MembershipType.PREMIUM.equals(getCurrentUser().getMembershipType())) {
            return maskMatchingWithScheduleResDto(result);
        }

        return result;
    }

    private UserSimpleResDto maskUserSimpleResDto(UserSimpleResDto dto) {
        return new UserSimpleResDto(
                dto.getId(),
                dto.getName(),
                dto.getBirth(),
                dto.getGender(),
                Const.DEFAULT_PROFILE // 프로필 URL 기본값 마스킹
        );
    }

    private MatchingWithScheduleResDto maskMatchingWithScheduleResDto(MatchingWithScheduleResDto dto) {
        return new MatchingWithScheduleResDto(
                dto.getId(),
                dto.getToScheduleId(),
                dto.getUserId(),
                dto.getUserName(),
                Const.DEFAULT_PROFILE, // 프로필 마스킹
                dto.getSimilarity(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }

    private RedisUserDto getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userDetails.getUser();
    }
}
