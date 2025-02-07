package com.example.connect.global.aop;

import com.example.connect.domain.match.dto.MatchingWithScheduleResDto;
import com.example.connect.domain.schedule.dto.ScheduleMatchingReqDto;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.user.dto.UserSimpleResDto;
import com.example.connect.domain.userimage.dto.UserImageDetailResDto;
import com.example.connect.domain.userimage.dto.UserImagePageResDto;
import com.example.connect.domain.userimage.dto.UserImageResDto;
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

import java.util.List;

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

    @Around(value = "@annotation(com.example.connect.global.aop.annotation.CheckMembership) && args(userId, id)",
            argNames = "joinPoint,userId,id")
    public Object checkUserFeedMembership(ProceedingJoinPoint joinPoint, Long userId, Long id) throws Throwable {

        UserImageDetailResDto result = (UserImageDetailResDto) joinPoint.proceed();

        if (!MembershipType.PREMIUM.equals(getCurrentUser().getMembershipType())) {

            return maskUserImageDetailResDto(result);
        }

        return result;
    }

    @Around(value = "@annotation(com.example.connect.global.aop.annotation.CheckMembership) && args(me, scheduleId, dto)",
            argNames = "joinPoint,me,scheduleId,dto")
    public Object checkMatchingMembership(ProceedingJoinPoint joinPoint, RedisUserDto me, Long scheduleId, ScheduleMatchingReqDto dto) throws Throwable {

        MatchingWithScheduleResDto result = (MatchingWithScheduleResDto) joinPoint.proceed();

        if (!MembershipType.PREMIUM.equals(getCurrentUser().getMembershipType())) {

            return maskMatchingWithScheduleResDto(result);
        }

        return result;
    }

    @Around(value = "@annotation(com.example.connect.global.aop.annotation.CheckMembership) && args(userId, page, size)",
            argNames = "joinPoint,userId,page,size")
    public Object checkUserImageMembership(ProceedingJoinPoint joinPoint, Long userId, int page, int size) throws Throwable {

        UserImagePageResDto result = (UserImagePageResDto) joinPoint.proceed();

        RedisUserDto me = getCurrentUser();

        if (userId.equals(me.getId())) {
            return result;
        }

        if (!MembershipType.PREMIUM.equals(me.getMembershipType())) {
            return maskUserImagePageResDto(result);
        }

        return result;
    }

    private UserSimpleResDto maskUserSimpleResDto(UserSimpleResDto dto) {
        return new UserSimpleResDto(
                dto.getId(),
                dto.getName(),
                dto.getBirth(),
                dto.getGender(),
                Const.DEFAULT_PROFILE,
                dto.getRateAvg()
        );
    }

    private MatchingWithScheduleResDto maskMatchingWithScheduleResDto(MatchingWithScheduleResDto dto) {
        return new MatchingWithScheduleResDto(
                dto.getId(),
                dto.getToScheduleId(),
                dto.getUserId(),
                dto.getUserName(),
                Const.DEFAULT_PROFILE,
                dto.getSimilarity(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }

    private UserImageDetailResDto maskUserImageDetailResDto(UserImageDetailResDto dto) {
        return new UserImageDetailResDto(
                dto.getId(),
                dto.getUrl(),
                dto.getDescription(),
                dto.getUserName(),
                dto.getUserId(),
                Const.DEFAULT_PROFILE,
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }

    private UserImagePageResDto maskUserImagePageResDto(UserImagePageResDto dto) {

        List<UserImageResDto> data = dto.getData();

        if (data.size() < Const.DEFAULT_FEED_SIZE) {
            return dto;
        }

        return new UserImagePageResDto(
                dto.getPage(),
                dto.getSize(),
                dto.getTotalElements(),
                dto.getTotalPages(),
                dto.getData().subList(0, Const.DEFAULT_FEED_SIZE)
        );
    }

    private RedisUserDto getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userDetails.getUser();
    }
}
