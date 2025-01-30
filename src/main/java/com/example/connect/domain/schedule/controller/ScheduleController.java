package com.example.connect.domain.schedule.controller;

import com.example.connect.domain.match.dto.MatchingListResDto;
import com.example.connect.domain.match.dto.MatchingWithScheduleResDto;
import com.example.connect.domain.match.service.MatchingService;
import com.example.connect.domain.schedule.dto.ScheduleMatchingReqDto;
import com.example.connect.domain.schedule.dto.ScheduleOnlyResDto;
import com.example.connect.domain.schedule.dto.SchedulePageResDto;
import com.example.connect.domain.schedule.dto.ScheduleReqDto;
import com.example.connect.domain.schedule.dto.ScheduleResDto;
import com.example.connect.domain.schedule.service.ScheduleService;
import com.example.connect.domain.schedulesubcategory.dto.ScheduleSubCategoryResDto;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.aop.annotation.SetMembershipFilter;
import com.example.connect.global.common.dto.CommonListResDto;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final MatchingService matchingService;

    @PostMapping
    public ResponseEntity<CommonResDto<ScheduleResDto>> createSchedule(
            @Valid @RequestBody ScheduleReqDto scheduleReqDto,
            Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        ScheduleResDto result = scheduleService.createSchedule(scheduleReqDto.toServiceDto(me.getId()));

        return new ResponseEntity<>(new CommonResDto<>("일정 생성 완료", result), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResDto<ScheduleResDto>> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleReqDto scheduleReqDto,
            Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        ScheduleResDto result = scheduleService.updateSchedule(id, scheduleReqDto.toServiceDto(me.getId()));

        return new ResponseEntity<>(new CommonResDto<>("일정 수정 완료", result), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id,
            Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        scheduleService.deleteSchedule(id, me.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/matchings")
    @SetMembershipFilter
    public ResponseEntity<CommonResDto<MatchingWithScheduleResDto>> matchSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleMatchingReqDto dto,
            Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        MatchingWithScheduleResDto result = matchingService.createMatching(me, id, dto);

        return new ResponseEntity<>(new CommonResDto<>("매칭 찾기 완료", result), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CommonResDto<SchedulePageResDto>> findAllSchedule(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        SchedulePageResDto result = scheduleService.findAllSchedules(me.getId(), date, page, size);

        return new ResponseEntity<>(new CommonResDto<>("일정 전체 조회 완료", result), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResDto<ScheduleOnlyResDto>> findSchedule(
            @PathVariable Long id
    ) {

        ScheduleOnlyResDto result = scheduleService.findScheduleById(id);

        return new ResponseEntity<>(new CommonResDto<>("일정 조회 완료", result), HttpStatus.OK);
    }

    @GetMapping("/{id}/sub-categories")
    public ResponseEntity<CommonListResDto<ScheduleSubCategoryResDto>> findScheduleContent(@PathVariable Long id) {

        List<ScheduleSubCategoryResDto> results = scheduleService.findScheduleContent(id);

        return new ResponseEntity<>(new CommonListResDto<>("일정 컨텐츠 조회 완료", results), HttpStatus.OK);
    }

    @GetMapping("/{id}/matchings")
    public ResponseEntity<CommonListResDto<MatchingListResDto>> findScheduleMatching(@PathVariable Long id) {

        List<MatchingListResDto> results = matchingService.findScheduleMatching(id);

        return new ResponseEntity<>(new CommonListResDto<>("일정 매칭 조회 완료", results), HttpStatus.OK);
    }
}
