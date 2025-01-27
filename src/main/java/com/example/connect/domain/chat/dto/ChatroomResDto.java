package com.example.connect.domain.chat.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import com.example.connect.global.enums.Gender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ChatroomResDto implements BaseDtoType {

    private final Long chatroomId;
    private final Long matchingId;
    private final LocalDate date;
    private final String title;
    private final String address;
    private final Long partnerId;
    private final String partnerName;
    private final Gender partnerGender;
}