package com.example.connect.domain.schedule.repository;

import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, CustomScheduleRepository {
    default Schedule findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    boolean existsScheduleByUserAndDate(User user, LocalDate date);

    @EntityGraph(attributePaths = {"user"})
    Optional<Schedule> findByIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT DISTINCT s
            FROM Schedule s
            LEFT JOIN FETCH s.scheduleContents sc
            JOIN FETCH s.user u
            JOIN FETCH sc.subCategory sc2
            JOIN FETCH sc2.category c
            WHERE u.gender = :gender
            AND s.id != :id
            AND u.birth BETWEEN :birthYearStart AND :birthYearEnd
            AND s.date = :date
            AND cast(FUNCTION('acos',
                cos(radians(:latitude)) * cos(radians(s.latitude)) *
                cos(radians(s.longitude) - radians(:longitude)) +
                sin(radians(:latitude)) * sin(radians(s.latitude))
            ) as double) * 6371 <= :distance
            ORDER BY cast(FUNCTION('acos',
                cos(radians(:latitude)) * cos(radians(s.latitude)) *
                cos(radians(s.longitude) - radians(:longitude)) +
                sin(radians(:latitude)) * sin(radians(s.latitude))
            ) as double) * 6371 ASC
            """)
    List<Schedule> findAllForMatching(
            @Param("id") Long id,
            @Param("gender") Gender gender,
            @Param("birthYearStart") String birthYearStart,
            @Param("birthYearEnd") String birthYearEnd,
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("distance") double distance,
            @Param("date") LocalDate date
    );
}
