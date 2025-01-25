package com.example.connect.domain.chat.service;

import com.example.connect.domain.chat.dto.ChatroomResDto;
import com.example.connect.domain.chat.entity.Chatroom;
import com.example.connect.domain.chat.entity.UserChatroom;
import com.example.connect.domain.chat.entity.enums.RoomStatus;
import com.example.connect.domain.chat.repository.ChatRepository;
import com.example.connect.domain.chat.repository.ChatroomRepository;
import com.example.connect.domain.chat.repository.UserChatroomRepository;
import com.example.connect.domain.match.repository.MatchingRepository;
import com.example.connect.domain.match.entity.Matching;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatRepository chatRepository;
    private final ChatroomRepository chatroomRepository;
    private final UserChatroomService userChatroomService;
    private final UserChatroomRepository userChatroomRepository;
    private final MatchingRepository matchingRepository;


    @Transactional
    public ChatroomResDto create(Long userId, Long matchingId) {

        Matching findMatching = matchingRepository.findByIdOrElseThrow(matchingId);

        Chatroom chatroom = new Chatroom(findMatching, RoomStatus.PRIVATE);

        Chatroom saveChatroom = chatroomRepository.save(chatroom);

        userChatroomService.save(userId, saveChatroom.getId());

        return saveChatroom.toDto();
    }

    @Transactional(readOnly = true)
    public List<ChatroomResDto> getChatroomList(Long userId) {
        return chatroomRepository
                .findAllByUserId(userId)
                .stream()
                .map(Chatroom::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long userId, Long roomId) {

        // 유저가 채팅방을 삭제하면, userChatroom 삭제
            // chat과 chatroom은 나머지 유저가 존재하면 계속 유지되어야 하기 때문에, 삭제하지 않음
        userChatroomRepository.deleteByUserIdAndChatroomId(userId, roomId);

        // 채팅방에 해당하는 유저가 없으면, 채팅방과 해당하는 채팅 내역 모두 삭제
            // userChatroom을 chatroomId로 조회 시, 아무것도 존재하지 않으면 chat, chatroom 삭제 처리
        Optional<UserChatroom> findUserChatroom = userChatroomRepository.findByChatroomId(roomId);

        if (!findUserChatroom.isPresent()) {
            deleteAllAboutChatAndChatroom(roomId);
        }
    }

    // 채팅 내역 및 채팅룸 삭제
    private void deleteAllAboutChatAndChatroom(Long chatroomId) {
        chatroomRepository.deleteById(chatroomId);
        chatRepository.deleteAllByChatroomId(chatroomId);
    }
}
