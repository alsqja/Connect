package com.example.connect.domain.chat.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateChatroomResDto implements BaseDtoType {

    private final Long chatroomId;

    @JsonCreator
    public CreateChatroomResDto(@JsonProperty("chatroomId") Long chatroomId) {
        this.chatroomId = chatroomId;
    }
}