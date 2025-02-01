package com.example.connect.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_chatroom_id", nullable = true)
    private UserChatroom userChatroom;

    @Column(name = "chatroom_id", nullable = false)
    private Long chatroomId;

    @Column(name="content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Chat(UserChatroom userChatroom, Long chatroomId, String content, LocalDateTime createdAt) {
        this.userChatroom = userChatroom;
        this.chatroomId = chatroomId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
