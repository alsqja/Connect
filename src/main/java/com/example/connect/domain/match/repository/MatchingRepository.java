package com.example.connect.domain.match.repository;

import com.example.connect.domain.match.dto.MatchingListResDto;
import com.example.connect.domain.match.entity.Matching;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

    default Matching findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query("""
            SELECT new com.example.connect.domain.match.dto.MatchingListResDto(
                m.id,
                ts.id,
                fs.id,
                ts.user.id,
                fs.user.id,
                ts.user.name,
                fs.user.name,
                ts.user.profileUrl,
                fs.user.profileUrl,
                m.similarity,
                m.status,
                m.createdAt,
                m.updatedAt
            ) FROM Matching m
            LEFT JOIN m.toSchedule ts
            LEFT JOIN m.fromSchedule fs
            WHERE (ts.id = :id OR fs.id = :id)
            AND (m.status = "PENDING" OR m.status = "ACCEPTED")
            """)
    List<MatchingListResDto> findDetailByScheduleId(Long id);

    @Query("""
            SELECT m FROM Matching m
                JOIN FETCH m.toSchedule ts
                JOIN FETCH ts.user
                JOIN FETCH m.fromSchedule fs
                JOIN FETCH fs.user
                WHERE m.id = :matchingId
            """)
    Optional<Matching> findByIdWithUser(@Param("matchingId") Long matchingId);

    @Query("""
            SELECT m FROM Matching m
                JOIN FETCH m.toSchedule ts
                JOIN FETCH ts.user
                JOIN FETCH m.fromSchedule fs
                JOIN FETCH fs.user
                WHERE ts.date = :date
                AND m.status = "ACCEPTED"
            """)
    List<Matching> findYesterdayMatching(LocalDate date);
}
