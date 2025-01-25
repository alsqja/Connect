package com.example.connect.domain.chat.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ChatroomResDto implements BaseDtoType {

    private final Long chatroomId;
    private final Long matchingId;
    private final LocalDateTime createdAt;
}
