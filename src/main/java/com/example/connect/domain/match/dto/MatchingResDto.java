package com.example.connect.domain.match.dto;

import com.example.connect.domain.match.entity.Matching;
import com.example.connect.global.common.dto.BaseDtoType;
import com.example.connect.global.enums.MatchStatus;
import lombok.Getter;

@Getter
public class MatchingResDto implements BaseDtoType {

    private final Long id;
    private final MatchStatus status;
    private final double similarity;

    public MatchingResDto(Matching matching) {
        this.id = matching.getId();
        this.status = matching.getStatus();
        this.similarity = matching.getSimilarity();
    }
}
