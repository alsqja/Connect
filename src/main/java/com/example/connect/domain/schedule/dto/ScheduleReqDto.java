package com.example.connect.domain.schedule.dto;

import com.example.connect.domain.schedulesubcategory.dto.ContentDescriptionDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ScheduleReqDto {

    @NotNull(message = "날짜를 입력해 주세요.")
    private final LocalDate date;

    @NotBlank(message = "일정 제목을 입력해 주세요.")
    private final String title;

    @NotBlank(message = "일정 설명을 입력해 주세요.")
    private final String details;

    @NotNull(message = "일정 항목들을 선택해 주세요.")
    @Size(min = 1, message = "한개 이상의 항목을 선택해 주세요.")
    private final List<ContentDescriptionDto> contents;

    @NotBlank(message = "주소를 입력해 주세요.")
    private final String address;

    @NotNull(message = "주소를 입력해 주세요.")
    private final Double latitude;

    @NotNull(message = "주소를 입력해 주세요.")
    private final Double longitude;

    public ScheduleServiceDto toServiceDto(Long userId) {
        return new ScheduleServiceDto(
                this.date,
                this.title,
                this.details,
                this.contents,
                userId,
                this.address,
                this.latitude,
                this.longitude
        );
    }
}
