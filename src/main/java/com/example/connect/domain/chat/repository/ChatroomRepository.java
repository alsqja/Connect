package com.example.connect.domain.chat.repository;

import com.example.connect.domain.chat.dto.ChatroomResDto;
import com.example.connect.domain.chat.entity.Chatroom;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    default Chatroom findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query("""
        SELECT new com.example.connect.domain.chat.dto.ChatroomResDto(
        cr.id,
        cr.matching.id,
        fs.date,
        fs.title,
        fs.address,
        t_u.id,
        t_u.name,
        t_u.gender
        ) FROM Chatroom cr
        JOIN UserChatroom uc ON uc.chatroom.id = cr.id
        JOIN cr.matching.fromSchedule fs
        JOIN cr.matching.toSchedule ts
        JOIN ts.user t_u
        WHERE uc.user.id = :userId
    """)
    List<ChatroomResDto> findAllChatroomByUserId(@Param("userId") Long userId);
}