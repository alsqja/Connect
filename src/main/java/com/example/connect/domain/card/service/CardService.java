package com.example.connect.domain.card.service;


import com.example.connect.domain.card.dto.CardReqDto;
import com.example.connect.domain.card.entity.Card;
import com.example.connect.domain.card.repository.CardRepository;
import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.global.util.AES256Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final AES256Util aes256Util;

    public void createCard(CardReqDto cardReqDto, Membership membership) {
        Card card = new Card(
                aes256Util.encrypt(cardReqDto.getNumber()),
                aes256Util.encrypt(cardReqDto.getExpiryYear()),
                aes256Util.encrypt(cardReqDto.getExpiryMonth()),
                aes256Util.encrypt(cardReqDto.getPasswordTwoDigits()),
                aes256Util.encrypt(cardReqDto.getBirthOrBusinessRegistrationNumber()),
                membership
        );

        cardRepository.save(card);
    }

    public CardReqDto decryptCard(Card card) {
        return new CardReqDto(
                aes256Util.decryptAES(card.getCardNumber()),
                aes256Util.decryptAES(card.getExpiredYear()),
                aes256Util.decryptAES(card.getExpiredMonth()),
                aes256Util.decryptAES(card.getCardPassword()),
                aes256Util.decryptAES(card.getBirth())
        );
    }
}
