package com.example.connect.domain.chat.dto;

import lombok.Getter;


@Getter
public class ChatRequestDto {

    private Long chatroomId;
    private Long senderId;
    private String content;
}
