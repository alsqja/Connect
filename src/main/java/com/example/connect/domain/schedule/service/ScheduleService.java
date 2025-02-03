package com.example.connect.domain.schedule.service;

import com.example.connect.domain.schedule.dto.ScheduleOnlyResDto;
import com.example.connect.domain.schedule.dto.SchedulePageResDto;
import com.example.connect.domain.schedule.dto.ScheduleResDto;
import com.example.connect.domain.schedule.dto.ScheduleServiceDto;
import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.schedule.repository.ScheduleRepository;
import com.example.connect.domain.schedulesubcategory.dto.ContentDescriptionDto;
import com.example.connect.domain.schedulesubcategory.dto.ScheduleSubCategoryResDto;
import com.example.connect.domain.schedulesubcategory.entity.ScheduleSubCategory;
import com.example.connect.domain.schedulesubcategory.repository.ScheduleSubCategoryRepository;
import com.example.connect.domain.subcategory.entity.SubCategory;
import com.example.connect.domain.subcategory.repository.SubCategoryRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.common.Const;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import com.example.connect.global.error.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ScheduleSubCategoryRepository scheduleSubCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Transactional
    public ScheduleResDto createSchedule(ScheduleServiceDto serviceDto) {

        if (serviceDto.getContents().size() > Const.SCHEDULE_CONTENTS_LIMIT) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        User user = userRepository.findByIdOrElseThrow(serviceDto.getUserId());

        if (scheduleRepository.existsScheduleByUserAndDate(user, serviceDto.getDate())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        Schedule schedule = serviceDto.toEntity();
        schedule.updateUser(user);

        Schedule savedSchedule = scheduleRepository.save(schedule);

        saveScheduleSubCategory(serviceDto, savedSchedule);

        return new ScheduleResDto(savedSchedule, serviceDto.getContents().stream().map(ContentDescriptionDto::getId).toList());
    }

    @Transactional
    public ScheduleResDto updateSchedule(Long id, ScheduleServiceDto serviceDto) {

        if (serviceDto.getContents().size() > Const.SCHEDULE_CONTENTS_LIMIT) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        Schedule schedule = scheduleRepository.findByIdOrElseThrow(id);

        if (!Objects.equals(schedule.getUser().getId(), serviceDto.getUserId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        schedule.updateField(serviceDto.toUpdateServiceDto());

        if (!serviceDto.getContents().isEmpty()) {
            scheduleSubCategoryRepository.deleteAllBySchedule(schedule);

            saveScheduleSubCategory(serviceDto, schedule);
        }

        return new ScheduleResDto(schedule, serviceDto.getContents().stream().map(ContentDescriptionDto::getId).toList());
    }

    private void saveScheduleSubCategory(ScheduleServiceDto serviceDto, Schedule schedule) {
        List<SubCategory> subCategories = subCategoryRepository.findAllById(serviceDto.getContents().stream().map(ContentDescriptionDto::getId).toList());

        if (subCategories.size() != serviceDto.getContents().size()) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        List<ScheduleSubCategory> scheduleSubCategories = serviceDto.getContents().stream().map(i -> new ScheduleSubCategory(i.getDescription(), schedule)).toList();

        for (int i = 0; i < subCategories.size(); i++) {
            scheduleSubCategories.get(i).updateSubCategory(subCategories.get(i));
        }

        scheduleSubCategoryRepository.saveAll(scheduleSubCategories);
    }

    @Transactional
    public void deleteSchedule(Long id, Long userId) {

        Schedule schedule = scheduleRepository.findByIdOrElseThrow(id);

        if (!Objects.equals(schedule.getUser().getId(), userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        scheduleRepository.delete(schedule);
    }

    public SchedulePageResDto findAllSchedules(Long userId, LocalDate date, int page, int size) {

        Pageable pageable = date == null ? PageRequest.of(page - 1, size) : PageRequest.of(0, 31);
        LocalDate startDate = date == null ? null : LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate endDate = date == null ? null : LocalDate.of(date.getYear(), date.getMonth().plus(1), 1).minusDays(1);

        Page<Schedule> schedulePage = scheduleRepository.findAllPageSchedule(pageable, startDate, endDate, userId);

        return new SchedulePageResDto(schedulePage);
    }

    public ScheduleOnlyResDto findScheduleById(Long id) {

        Schedule schedule = scheduleRepository.findByIdOrElseThrow(id);

        return new ScheduleOnlyResDto(schedule);
    }

    public List<ScheduleSubCategoryResDto> findScheduleContent(Long id) {

        return subCategoryRepository.findDetailsByScheduleId(id);
    }
}
