package com.example.connect.domain.chat.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatResDto {

    private Long chatrommId;
    private Long sender;
    private String content;
    private LocalDateTime createdAt;

    public ChatResDto(Long chatrommId, Long sender, String content, LocalDateTime createdAt) {
        this.chatrommId = chatrommId;
        this.sender = sender;
        this.content = content;
        this.createdAt = createdAt;
    }
}
