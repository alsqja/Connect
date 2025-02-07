package com.example.connect.domain.payment.dto;

import com.example.connect.domain.card.dto.CardReqDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AutoPaymentReqDto {
    private final String channelKey;
    private final String orderName;
    private final PaidPaymentAmountDto amount;
    private final String currency;
    private final Method method;

    @Getter
    @AllArgsConstructor
    public static class Method {
        private CardData card;
    }

    @Getter
    @AllArgsConstructor
    public static class CardData {
        private final CardReqDto credential;
    }
}
