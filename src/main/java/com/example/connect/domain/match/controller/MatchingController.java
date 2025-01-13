package com.example.connect.domain.match.controller;

import com.example.connect.domain.match.dto.MatchingResDto;
import com.example.connect.domain.match.dto.UpdateMatchReqDto;
import com.example.connect.domain.match.service.MatchingService;
import com.example.connect.global.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matchings")
public class MatchingController {

    private final MatchingService matchingService;

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResDto<MatchingResDto>> update(
            @PathVariable Long id,
            @RequestBody UpdateMatchReqDto dto
    ) {

        MatchingResDto result = matchingService.updateMatchingStatus(id, dto.getStatus());

        return new ResponseEntity<>(new CommonResDto<>("매칭 수정 완료", result), HttpStatus.OK);
    }
}
