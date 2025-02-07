package com.example.connect.domain.match.controller;

import com.example.connect.domain.chat.dto.SimpleChatroomResDto;
import com.example.connect.domain.chat.service.ChatroomService;
import com.example.connect.domain.match.dto.MatchingPartnerResDto;
import com.example.connect.domain.match.dto.MatchingResDto;
import com.example.connect.domain.match.dto.UpdateMatchReqDto;
import com.example.connect.domain.match.service.MatchingService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final ChatroomService chatroomService;

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResDto<MatchingResDto>> update(
            @PathVariable Long id,
            @RequestBody UpdateMatchReqDto dto
    ) {

        MatchingResDto result = matchingService.updateMatchingStatus(id, dto.getStatus());

        return new ResponseEntity<>(new CommonResDto<>("매칭 수정 완료", result), HttpStatus.OK);
    }

    @GetMapping("/{matchingId}/chatrooms")
    public ResponseEntity<CommonResDto<SimpleChatroomResDto>> getMatchingChatRoom(@PathVariable Long matchingId) {

        SimpleChatroomResDto result = chatroomService.findByMatchingId(matchingId);

        return new ResponseEntity<>(new CommonResDto<>("채팅방 조회 완료", result), HttpStatus.OK);
    }

    @GetMapping("/{matchingId}/partners")
    public ResponseEntity<CommonResDto<MatchingPartnerResDto>> getMatching(
            @PathVariable Long matchingId,
            Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();
        
        MatchingPartnerResDto result = matchingService.findMatchingPartner(me.getId(), matchingId);

        return new ResponseEntity<>(new CommonResDto<>("상대방 조회 완료", result), HttpStatus.OK);
    }
}
