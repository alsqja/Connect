package com.example.connect.domain.card.dto;

import com.example.connect.domain.card.entity.Card;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CardResDto implements BaseDtoType {
    private final Long id;
    private final String cardNumber;
    private final String expiredYear;
    private final String expiredMonth;
    private final String cardPassword;
    private final String birth;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CardResDto(Card card) {
        this.id = card.getId();
        this.cardNumber = card.getCardNumber();
        this.expiredYear = card.getExpiredYear();
        this.expiredMonth = card.getExpiredMonth();
        this.cardPassword = card.getCardPassword();
        this.birth = card.getBirth();
        this.createdAt = card.getCreatedAt();
        this.updatedAt = card.getUpdatedAt();
    }
}
