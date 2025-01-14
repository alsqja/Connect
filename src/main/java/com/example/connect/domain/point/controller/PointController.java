package com.example.connect.domain.point.controller;

import com.example.connect.domain.point.dto.PointUpdateReqDto;
import com.example.connect.domain.point.service.PointService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
