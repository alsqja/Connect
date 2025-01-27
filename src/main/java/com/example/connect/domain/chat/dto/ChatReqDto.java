package com.example.connect.domain.chat.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor
public class ChatReqDto {

    private final Long userId;
    private final String name;
    private final String image;
    private final String message;
    private final LocalDateTime time;
}
