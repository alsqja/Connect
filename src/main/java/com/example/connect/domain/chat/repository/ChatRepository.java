package com.example.connect.domain.chat.repository;

import com.example.connect.domain.chat.entity.Chat;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    void deleteAllByChatroomId(Long chatroomId);

    @Modifying
    @Query("UPDATE Chat c SET c.userChatroom = NULL WHERE c.userChatroom.id = :userChatroomId")
    void setUserChatroomIdToNull(@Param("userChatroomId") Long userChatroomId);
}
