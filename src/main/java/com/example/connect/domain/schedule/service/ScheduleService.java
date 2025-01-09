package com.example.connect.domain.schedule.service;

import com.example.connect.domain.schedule.dto.ScheduleResDto;
import com.example.connect.domain.schedule.dto.ScheduleServiceDto;
import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.schedule.repository.ScheduleRepository;
import com.example.connect.domain.schedulesubcategory.dto.ContentDescriptionDto;
import com.example.connect.domain.schedulesubcategory.entity.ScheduleSubCategory;
import com.example.connect.domain.schedulesubcategory.repository.ScheduleSubCategoryRepository;
import com.example.connect.domain.subcategory.entity.SubCategory;
import com.example.connect.domain.subcategory.repository.SubCategoryRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ScheduleSubCategoryRepository scheduleSubCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Transactional
    public ScheduleResDto createSchedule(ScheduleServiceDto serviceDto) {

        User user = userRepository.findByIdOrElseThrow(serviceDto.getUserId());

        if (scheduleRepository.existsScheduleByUserAndDate(user, serviceDto.getDate())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        Schedule schedule = serviceDto.toEntity();
        schedule.updateUser(user);

        Schedule savedSchedule = scheduleRepository.save(schedule);

        List<SubCategory> subCategories = subCategoryRepository.findAllById(serviceDto.getContents().stream().map(ContentDescriptionDto::getId).toList());

        if (subCategories.size() != serviceDto.getContents().size()) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        List<ScheduleSubCategory> scheduleSubCategories = serviceDto.getContents().stream().map(i -> new ScheduleSubCategory(i.getDescription(), savedSchedule)).toList();

        for (int i = 0; i < subCategories.size(); i++) {
            scheduleSubCategories.get(i).updateSubCategory(subCategories.get(i));
        }

        scheduleSubCategoryRepository.saveAll(scheduleSubCategories);

        return new ScheduleResDto(savedSchedule, serviceDto.getContents().stream().map(ContentDescriptionDto::getId).toList());
    }
}
