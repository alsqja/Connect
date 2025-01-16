package com.example.connect.domain.chat.service;

import com.example.connect.domain.chat.entity.Chatroom;
import com.example.connect.domain.chat.entity.enums.RoomStatus;
import com.example.connect.domain.chat.repository.ChatroomRepository;
import com.example.connect.domain.match.repository.MatchingRepository;
import com.example.connect.domain.match.entity.Matching;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final MatchingRepository matchingRepository;

    public Long create(Long matchingId) {

        Matching findMatching = matchingRepository.findByIdOrElseThrow(matchingId);

        Chatroom chatroom = new Chatroom(findMatching, RoomStatus.PRIVATE);

        Chatroom saveChatroom = chatroomRepository.save(chatroom);

        return saveChatroom.getId();
    }
}
