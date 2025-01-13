package com.example.connect.domain.schedule.repository;

import com.example.connect.domain.schedule.entity.QSchedule;
import com.example.connect.domain.schedule.entity.Schedule;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomScheduleRepositoryImpl implements CustomScheduleRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Schedule> findAllPageSchedule(Pageable pageable, LocalDate startDate, LocalDate endDate, Long userId) {

        QSchedule schedule = QSchedule.schedule;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(schedule.user.id.eq(userId));
        if (startDate != null) {
            builder.and(schedule.date.goe(startDate));
        }
        if (endDate != null) {
            builder.and(schedule.date.loe(endDate));
        }


        List<Schedule> results = jpaQueryFactory.selectFrom(schedule)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                jpaQueryFactory.select(schedule.count())
                        .from(schedule)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }

//    @Override
//    public Page<ScheduleWithContentNameResDto> findAllPageSchedule(Pageable pageable, LocalDate startDate, LocalDate endDate, Long userId) {
//
//        QSchedule schedule = QSchedule.schedule;
//        QSubCategory subCategory = QSubCategory.subCategory;
//        QScheduleSubCategory scheduleSubCategory = QScheduleSubCategory.scheduleSubCategory;
//
//        BooleanBuilder builder = new BooleanBuilder();
//
//        builder.and(schedule.user.id.eq(userId));
//        if (startDate != null) {
//            builder.and(schedule.date.goe(startDate));
//        }
//        if (endDate != null) {
//            builder.and(schedule.date.loe(endDate));
//        }
//
//        List<ScheduleWithContentNameResDto> results = jpaQueryFactory.select(
//                        Projections.constructor(
//                                ScheduleWithContentNameResDto.class,
//                                schedule.id,
//                                schedule.date,
//                                schedule.title,
//                                schedule.details,
//                                Expressions.stringTemplate(
//                                        "group_concat({0})",
//                                        subCategory.name
//                                ).as("contentNames"),
//                                schedule.createdAt,
//                                schedule.updatedAt
//                        )
//                ).from(schedule)
//                .leftJoin(scheduleSubCategory).on(scheduleSubCategory.schedule.id.eq(schedule.id))
//                .leftJoin(subCategory).on(scheduleSubCategory.subCategory.id.eq(subCategory.id))
//                .where(builder)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        long total = Optional.ofNullable(
//                jpaQueryFactory.select(schedule.count())
//                        .from(schedule)
//                        .fetchOne()
//        ).orElse(0);
//
//        return new PageImpl<>(results, pageable, total);
//    }


}
