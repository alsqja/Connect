package com.example.connect.domain.chat.repository;

import com.example.connect.domain.chat.dto.ChatResDto;
import com.example.connect.domain.chat.entity.UserChatroom;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserChatroomRepository extends JpaRepository<UserChatroom, Long> {

    Optional<UserChatroom> findByUserId(Long userId);

    default UserChatroom findByUserIdOrElseThrow(Long userId) {
        return findByUserId(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    Optional<UserChatroom> findByChatroomId(Long chatroomId);

    @Query("""
            SELECT new com.example.connect.domain.chat.dto.ChatResDto(
                cr.id,
                uc.user.id,
                c.content,
                c.createdAt
            ) FROM UserChatroom uc
            JOIN uc.chatroom cr
            JOIN Chat c ON uc.id = c.userChatroom.id
            WHERE cr.id = :chatroomId
            ORDER BY c.createdAt DESC
            """)
    Page<ChatResDto> findTopChatsByChatroomId(@Param("chatroomId") Long chatroomId, Pageable pageable);

    void deleteByUserIdAndChatroomId(Long userId, Long ChatroomId);
}
