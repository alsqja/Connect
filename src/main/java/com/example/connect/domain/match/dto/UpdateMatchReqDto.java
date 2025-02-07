package com.example.connect.domain.match.dto;

import com.example.connect.global.enums.MatchStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateMatchReqDto {

    private final MatchStatus status;
}
