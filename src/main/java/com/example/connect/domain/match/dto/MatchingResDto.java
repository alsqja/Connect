package com.example.connect.domain.match.dto;

import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MatchingResDto implements BaseDtoType {

    private final Long id;
    private final Long toScheduleId;
    private final Long userId;
    private final String userName;
    private final String profileUrl;
    private final double similarity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public MatchingResDto(Schedule schedule, Matching matching) {
        this.id = matching.getId();
        this.toScheduleId = schedule.getId();
        this.userId = schedule.getUser().getId();
        this.userName = schedule.getUser().getName();
        this.profileUrl = schedule.getUser().getProfileUrl();
        this.similarity = matching.getSimilarity();
        this.createdAt = matching.getCreatedAt();
        this.updatedAt = matching.getUpdatedAt();
    }
}
