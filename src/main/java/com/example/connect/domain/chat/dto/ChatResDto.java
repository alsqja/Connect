package com.example.connect.domain.chat.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ChatResDto implements BaseDtoType {

    private final Long chatrommId;          // 채팅방 ID
    private final Long sender;              // 메세지 발생자
    private final String profileUrl;          // 유저 프로필 이미지
    private final String message;           // 메제지
    private final LocalDateTime createdAt;  // 생성 일자
}
