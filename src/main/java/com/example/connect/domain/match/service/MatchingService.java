package com.example.connect.domain.match.service;

import com.example.connect.domain.match.dto.MatchingListResDto;
import com.example.connect.domain.match.dto.MatchingResDto;
import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.match.repository.MatchingRepository;
import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.schedule.repository.ScheduleRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.MatchStatus;
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

    @Transactional
    public MatchingResDto createMatching(Long userId, Long scheduleId) {

        Schedule schedule = scheduleRepository.findByIdAndUserId(scheduleId, userId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.BAD_REQUEST));

        User user = schedule.getUser(); // q + 1
        Gender gender = user.getGender().equals(Gender.MAN) ? Gender.WOMAN : Gender.MAN;
        String birth = user.getBirth();
        String start = Integer.toString(Integer.parseInt(birth.substring(0, 4)) - 5);
        String end = Integer.toString(Integer.parseInt(birth.substring(0, 4)) + 5);

        List<Schedule> scheduleList = scheduleRepository.findAllForMatching(
                scheduleId,
                gender,
                start,
                end,
                schedule.getLatitude(),
                schedule.getLongitude(),
                10,
                schedule.getDate()
        );

        if (schedule.getCount() > 0) {
            int count = schedule.getCount();
            scheduleList.subList(0, count).clear();
        }

        if (scheduleList.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        Jaccard jaccard = new Jaccard();
        for (Schedule otherSchedule : scheduleList) {
            jaccard.addSimilaritySchedule(schedule, otherSchedule);
        }

        int biggestIndex = jaccard.getBiggestIndex();
        double biggestSimilarity = jaccard.getBiggestSimilarity() > 0 ? jaccard.getBiggestSimilarity() : 0;

        Matching matching = new Matching(MatchStatus.CREATED, schedule, scheduleList.get(biggestIndex), biggestSimilarity);
        matchingRepository.save(matching);

        schedule.addCount();

        return new MatchingResDto(scheduleList.get(biggestIndex), matching);
    }

    public List<MatchingListResDto> findScheduleMatching(Long id) {

        return matchingRepository.findDetailByScheduleId(id);
    }
}
