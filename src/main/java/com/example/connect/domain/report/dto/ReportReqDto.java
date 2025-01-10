package com.example.connect.domain.report.dto;

import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReportReqDto {

    private final Long toId;
    private final Long matchingId;
    private final String content;
}
