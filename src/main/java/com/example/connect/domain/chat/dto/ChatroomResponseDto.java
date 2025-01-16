package com.example.connect.domain.chat.dto;

import com.example.connect.domain.chat.entity.enums.RoomStatus;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatroomResponseDto implements BaseDtoType {

    private Long chatroomId;
    private Long matchingId;
    private LocalDateTime createdAt;

    public ChatroomResponseDto(Long chatroomId, Long matchingId, LocalDateTime createdAt) {
        this.chatroomId = chatroomId;
        this.matchingId = matchingId;
        this.createdAt = createdAt;
    }
}
