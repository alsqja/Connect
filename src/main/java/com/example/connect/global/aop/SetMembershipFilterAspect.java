package com.example.connect.global.aop;

import com.example.connect.domain.schedule.dto.ScheduleMatchingReqDto;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.common.Const;
import com.example.connect.global.config.auth.UserDetailsImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SetMembershipFilterAspect {

    @Around("@annotation(com.example.connect.global.aop.annotation.SetMembershipFilter)")
    public Object setMembershipFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        RedisUserDto me = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ScheduleMatchingReqDto dto) {
                if (me.getMembershipType() == null) {
                    dto = new ScheduleMatchingReqDto(
                            Const.NO_MEMBERSHIP_GENDER_FILTER,
                            Const.DEFAULT_MINUS_AGE,
                            Const.DEFAULT_PLUS_AGE,
                            Const.DEFAULT_MATCHING_DISTANCE
                    );
                }
                args[i] = dto;
            }
        }

        return joinPoint.proceed(args);
    }
}
