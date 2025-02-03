package com.example.connect.domain.match.service;

import com.example.connect.domain.match.dto.MatchingListResDto;
import com.example.connect.domain.match.dto.MatchingResDto;
import com.example.connect.domain.match.dto.MatchingWithScheduleResDto;
import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.match.repository.MatchingRepository;
import com.example.connect.domain.point.service.PointService;
import com.example.connect.domain.schedule.dto.ScheduleMatchingReqDto;
import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.schedule.repository.ScheduleRepository;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.aop.annotation.CheckMembership;
import com.example.connect.global.aop.annotation.SendNotify;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.MatchStatus;
import com.example.connect.global.enums.MembershipType;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import com.example.connect.global.error.exception.NotFoundException;
import com.example.connect.global.util.Jaccard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final ScheduleRepository scheduleRepository;
    private final PointService pointService;

    @Transactional
    @CheckMembership
    public MatchingWithScheduleResDto createMatching(RedisUserDto me, Long scheduleId, ScheduleMatchingReqDto dto) {

        Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId, me.getId())
                .orElseThrow(() -> new BadRequestException(ErrorCode.BAD_REQUEST));

        if (schedule.getCount() >= 5 && (me.getMembershipType() == null || !me.getMembershipType().equals(MembershipType.PREMIUM))) {
            pointService.usePoint(me.getId(), 1L, "매칭 1회: 50 포인트 사용");
        }

        User user = schedule.getUser();
        Gender gender = dto.getGender();
        String birth = user.getBirth();
        String start = Integer.toString(Integer.parseInt(birth.substring(0, 4)) - dto.getMinusAge());
        String end = Integer.toString(Integer.parseInt(birth.substring(0, 4)) + dto.getPlusAge());

        List<Schedule> scheduleList = scheduleRepository.findAllForMatching(
                scheduleId,
                me.getId(),
                gender,
                start,
                end,
                schedule.getLatitude(),
                schedule.getLongitude(),
                dto.getDistance(),
                schedule.getDate()
        );

        if (scheduleList.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        Jaccard jaccard = new Jaccard();
        int myIdx = -1;

        for (int i = 0; i < scheduleList.size(); i++) {
            if (!schedule.getId().equals(scheduleList.get(i).getId())) {
                jaccard.addSimilaritySchedule(schedule, scheduleList.get(i));
            } else {
                myIdx = i;
            }
        }

        if (myIdx >= 0) {
            scheduleList.remove(myIdx);
        }

        if (scheduleList.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        int biggestIndex = jaccard.getBiggestIndex();
        double biggestSimilarity = jaccard.getBiggestSimilarity() > 0 ? jaccard.getBiggestSimilarity() : 0;

        Matching matching = new Matching(MatchStatus.CREATED, schedule, scheduleList.get(biggestIndex), biggestSimilarity);
        matchingRepository.save(matching);

        schedule.addCount();

        return new MatchingWithScheduleResDto(scheduleList.get(biggestIndex), matching);
    }

    public List<MatchingListResDto> findScheduleMatching(Long id) {

        return matchingRepository.findDetailByScheduleId(id);
    }

    @Transactional
    @SendNotify
    public MatchingResDto updateMatchingStatus(Long id, MatchStatus status) {

        Matching matching = matchingRepository.findByIdOrElseThrow(id);

        matching.updateStatus(status);

        return new MatchingResDto(matching);
    }
}
