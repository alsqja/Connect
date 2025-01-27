package com.example.connect.domain.chat.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor
public class ChatReqDto {

    private final Long senderId;           // 채팅 작성자 ID
    private final String name;             // 채팅 작성자 이름
    private final String profileUrl;        // 채팅 작성자 프로필
    private final String message;          // 채팅 메세지;      // 채팅 생성 시간
}