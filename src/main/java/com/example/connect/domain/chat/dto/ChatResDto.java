package com.example.connect.domain.chat.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ChatResDto implements BaseDtoType {

    private final Long chatrommId;          // 채팅방 ID
    private final Long senderId;            // 채팅 작성자 ID
    private final String name;              // 채팅 작성자 이름
    private final String profileUrl;         // 채팅 작성자 프로필 이미지
    private final String message;           // 메제지
    private final LocalDateTime createdAt;  // 생성 일자

    public ChatResDto(Long chatrommId, ChatReqDto chatReqDto, LocalDateTime createdAt) {
        this.chatrommId = chatrommId;
        this.senderId = chatReqDto.getSenderId();
        this.name = chatReqDto.getName();
        this.profileUrl = chatReqDto.getProfileUrl();
        this.message = chatReqDto.getMessage();
        this.createdAt = createdAt;
    }
}