package com.example.connect.domain.point.controller;

import com.example.connect.domain.point.dto.PointUpdateReqDto;
import com.example.connect.domain.point.service.PointService;
import com.example.connect.domain.pointuse.dto.PointUseListResDto;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
public class PointController {
    private final PointService pointService;

    @PostMapping
    public ResponseEntity<Void> usePoint(
            Authentication authentication,
            @RequestBody PointUpdateReqDto pointUpdateReqDto
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        pointService.usePoint(me.getId(), pointUpdateReqDto.getMatchingCount(), pointUpdateReqDto.getDetails());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<CommonResDto<PointUseListResDto>> getPoints(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        PointUseListResDto pointList = pointService.getAllPoint(me.getId(), page, size);

        return new ResponseEntity<>(new CommonResDto<>("포인트 내역 조회 완료.", pointList), HttpStatus.OK);
    }
}
