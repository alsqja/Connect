package com.example.connect.domain.schedule.controller;

import com.example.connect.domain.schedule.dto.ScheduleReqDto;
import com.example.connect.domain.schedule.dto.ScheduleResDto;
import com.example.connect.domain.schedule.service.ScheduleService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

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
}
