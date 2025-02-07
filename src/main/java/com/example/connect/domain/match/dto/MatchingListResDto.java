package com.example.connect.domain.match.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import com.example.connect.global.enums.MatchStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MatchingListResDto implements BaseDtoType {

    private final Long id;
    private final Long toScheduleId;
    private final Long fromScheduleId;
    private final Long toUserId;
    private final Long fromUserId;
    private final String toUserName;
    private final String fromUserName;
    private final String toUserProfileUrl;
    private final String fromUserProfileUrl;
    private final Double similarity;
    private final MatchStatus matchStatus;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
