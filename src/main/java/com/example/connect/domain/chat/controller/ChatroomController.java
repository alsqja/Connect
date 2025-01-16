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
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{roomId}")
    public ResponseEntity delete(
            @PathVariable Long roomId,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        chatroomService.delete(me.getId(), roomId);

        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

