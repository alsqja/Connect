package com.example.connect.domain.chat.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SimpleChatroomResDto implements BaseDtoType {

    private final Long chatroomId;
    private final Long matchingId;
}
