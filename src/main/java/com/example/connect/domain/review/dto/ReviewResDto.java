package com.example.connect.domain.review.dto;

import com.example.connect.domain.review.entity.Review;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReviewResDto implements BaseDtoType {

    private final Long id;
    private final Long toId;
    private final Long matchingId;
    private final Integer rate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ReviewResDto(Review review) {
        this.id = review.getId();
        this.toId = review.getToUser().getId();
        this.matchingId = review.getMatching().getId();
        this.rate = review.getRate();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}
