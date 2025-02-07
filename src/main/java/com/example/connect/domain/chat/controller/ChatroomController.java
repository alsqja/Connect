package com.example.connect.domain.chat.controller;

import com.example.connect.domain.chat.dto.ChatResDto;
import com.example.connect.domain.chat.dto.ChatroomResDto;
import com.example.connect.domain.chat.dto.CreateChatroomResDto;
import com.example.connect.domain.chat.service.ChatroomService;
import com.example.connect.domain.chat.service.UserChatroomService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.common.dto.CommonListResDto;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService chatroomService;
    private final UserChatroomService userChatroomService;

    /**
     * 채팅방 생성
     *
     * @param authentication 인증 정보
     * @return {@link CreateChatroomResDto} 생성된 채팅방 정보
     */
    @PostMapping("/{matchingId}")
    public ResponseEntity<CommonResDto<CreateChatroomResDto>> create(
            @PathVariable Long matchingId,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        CreateChatroomResDto result = chatroomService.create(me.getId(), matchingId);

        return new ResponseEntity<>(new CommonResDto<>("채팅방 셍성", result), HttpStatus.OK);
    }

    /**
     * 채팅방 입장 및 내역 조회
     *
     * @param roomId         채팅방 ID
     * @param authentication 인증 정보
     * @return List of {@link ChatResDto} 채팅 내역
     */
    @GetMapping("/history/{roomId}")
    public ResponseEntity<CommonListResDto<ChatResDto>> getChatHistory(
            @PathVariable Long roomId,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        List<ChatResDto> chatHistory = userChatroomService.getChatHistory(me.getId(), roomId);

        return new ResponseEntity<>(new CommonListResDto<>("채팅 내역 조회", chatHistory), HttpStatus.OK);
    }

    /**
     * 채팅방 내역 조회
     *
     * @param authentication
     * @return List of {@link ChatroomResDto} 채팅방 정보 리스트
     */
    @GetMapping
    public ResponseEntity<CommonListResDto<ChatroomResDto>> getChatroomList(
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        List<ChatroomResDto> results = chatroomService.getChatroomList(me.getId());

        return new ResponseEntity<>(new CommonListResDto<>("채팅방 리스트 조회", results), HttpStatus.OK);
    }

    /**
     * 채팅방 삭제
     *
     * @param roomId
     * @param authentication
     * @return
     */
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long roomId,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        chatroomService.leaveChatroom(me.getId(), roomId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

