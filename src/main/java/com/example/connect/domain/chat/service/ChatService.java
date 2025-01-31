package com.example.connect.domain.chat.service;

import com.example.connect.domain.chat.dto.ChatReqDto;
import com.example.connect.domain.chat.entity.Chat;
import com.example.connect.domain.chat.entity.UserChatroom;
import com.example.connect.domain.chat.repository.ChatRepository;
import com.example.connect.domain.chat.repository.UserChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserChatroomRepository userChatroomRepository;

    /**
     * 채팅 저장
     */
    @Transactional
    public void save(Long roomId, ChatReqDto message, LocalDateTime current) {

        UserChatroom findUserChatroom = userChatroomRepository.findByUserIdAndChatroomIdOrElseThrow(message.getSenderId(), roomId);

        Chat chat = new Chat(findUserChatroom, roomId, message.getMessage(), current);

        chatRepository.save(chat);
    }
}
