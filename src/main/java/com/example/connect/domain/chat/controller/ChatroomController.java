package com.example.connect.domain.chat.controller;

import com.example.connect.domain.chat.dto.ChatroomResponseDto;
import com.example.connect.domain.chat.service.ChatroomService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.common.dto.CommonListResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService chatroomService;

    @GetMapping
    public ResponseEntity<CommonListResDto<ChatroomResponseDto>> getChatroomList(
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        List<ChatroomResponseDto> results = chatroomService.getChatroomList(me.getId());

        return new ResponseEntity<>(new CommonListResDto<>("채팅방 리스트 조회", results), HttpStatus.OK);
    }

    // TODO: 채팅방 삭제 API - 각 채팅방에 채팅방에 존재하는 유저들의 카운트를 추가하여 카운트가 0 이 되면, 채팅방, 채팅 내역 삭제 처리

    //
}

