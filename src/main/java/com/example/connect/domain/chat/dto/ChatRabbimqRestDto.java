package com.example.connect.domain.chat.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatRabbimqRestDto {

    private Long chatrommId;          // 채팅방 ID
    private Long senderId;            // 채팅 작성자 ID
    private String name;              // 채팅 작성자 이름
    private String profileUrl;         // 채팅 작성자 프로필 이미지
    private String message;           // 메제지
    private LocalDateTime createdAt;  // 생성 일자

    public ChatRabbimqRestDto(Long chatrommId, ChatReqDto chatReqDto, LocalDateTime createdAt) {
        this.chatrommId = chatrommId;
        this.senderId = chatReqDto.getSenderId();
        this.name = chatReqDto.getName();
        this.profileUrl = chatReqDto.getProfileUrl();
        this.message = chatReqDto.getMessage();
        this.createdAt = createdAt;
    }
}
