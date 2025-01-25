package com.example.connect.domain.chat.service;

import com.example.connect.domain.chat.dto.ChatReqDto;
import com.example.connect.domain.chat.entity.Chat;
import com.example.connect.domain.chat.entity.UserChatroom;
import com.example.connect.domain.chat.repository.ChatRepository;
import com.example.connect.domain.chat.repository.UserChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserChatroomRepository userChatroomRepository;

    @Transactional
    public void save(Long roomId, ChatReqDto message) {

        UserChatroom findUserChatroom = userChatroomRepository.findByUserIdOrElseThrow(message.getSenderId());

        Chat chat = new Chat(findUserChatroom, roomId, message.getContent());

        chatRepository.save(chat);
    }
}
