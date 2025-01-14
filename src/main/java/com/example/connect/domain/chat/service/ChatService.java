package com.example.connect.domain.chat.service;

import com.example.connect.domain.chat.dto.ChatRequestDto;
import com.example.connect.domain.chat.entity.Chat;
import com.example.connect.domain.chat.entity.UserChatroom;
import com.example.connect.domain.chat.repository.ChatRepository;
import com.example.connect.domain.chat.repository.UserChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserChatroomRepository userChatroomRepository;

    public void save(ChatRequestDto message) {

        UserChatroom findUserChatroom = userChatroomRepository.findByUserIdOrElseThrow(message.getSenderId());

        Chat chat = new Chat(findUserChatroom, message.getContent());

        chatRepository.save(chat);
    }
}
