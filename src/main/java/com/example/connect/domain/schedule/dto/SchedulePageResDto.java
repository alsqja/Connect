package com.example.connect.domain.schedule.dto;

import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SchedulePageResDto implements BaseDtoType {

    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final List<ScheduleWithContentNameResDto> data;

    public SchedulePageResDto(Page<Schedule> schedulePage) {
        this.page = schedulePage.getNumber() + 1;
        this.size = schedulePage.getSize();
        this.totalElements = schedulePage.getTotalElements();
        this.totalPages = schedulePage.getTotalPages();
        this.data = schedulePage.getContent().stream().map(ScheduleWithContentNameResDto::new).toList();
    }
}
