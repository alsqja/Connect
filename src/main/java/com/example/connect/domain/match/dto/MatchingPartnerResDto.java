package com.example.connect.domain.match.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MatchingPartnerResDto implements BaseDtoType {

    private final Long partnerId;
}
