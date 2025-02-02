package com.example.connect.domain.chat.repository;

import com.example.connect.domain.chat.dto.ChatResDto;
import com.example.connect.domain.chat.entity.UserChatroom;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.ForbiddenException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChatroomRepository extends JpaRepository<UserChatroom, Long> {
    Optional<UserChatroom> findByUserIdAndChatroomId(Long userId, Long chatroomId);

    void deleteAllByChatroomId(Long chatroomId);

    boolean existsByChatroomIdAndIsDeleteFalse(Long chatroomId);

    default UserChatroom findByUserIdAndChatroomIdOrElseThrow(Long userId, Long chatroomId) {
        return findByUserIdAndChatroomId(userId, chatroomId).orElseThrow(
                ()-> new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION));
    }

    Optional<UserChatroom> findByChatroomId(Long chatroomId);

    @Query("""
        SELECT new com.example.connect.domain.chat.dto.ChatResDto(
            cr.id,
            uc.user.id,
            u.name,
            u.profileUrl,
            c.content,
            c.createdAt
        ) FROM UserChatroom uc
        JOIN uc.chatroom cr
        JOIN uc.user u
        JOIN Chat c ON uc.id = c.userChatroom.id
        WHERE cr.id = :chatroomId
        ORDER BY c.createdAt DESC
        """)
    List<ChatResDto> findTopChatsByChatroomId(@Param("chatroomId") Long chatroomId);

    void deleteByUserIdAndChatroomId(Long userId, Long ChatroomId);
}
