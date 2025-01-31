package com.example.connect.domain.chat.service;

import com.example.connect.domain.chat.dto.ChatroomResDto;
import com.example.connect.domain.chat.dto.CreateChatroomResDto;
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

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatRepository chatRepository;
    private final ChatroomRepository chatroomRepository;
    private final UserChatroomService userChatroomService;
    private final UserChatroomRepository userChatroomRepository;
    private final MatchingRepository matchingRepository;

    /**
     *  채팅방 생성
     */
    @Transactional
    public CreateChatroomResDto create(Long userId, Long matchingId) {

        Matching findMatching = matchingRepository.findByIdOrElseThrow(matchingId);

        Chatroom chatroom = new Chatroom(findMatching, RoomStatus.PRIVATE);

        Chatroom saveChatroom = chatroomRepository.save(chatroom);

        // TODO: 매칭이 성사 되는 두 유저 모두 userChatroom이 생성되어야 한다. -> 해당 과정이 이뤄지는지 검증 필요
            // 1. 매칭 성사 이후의 채팅방 API 호출을  클라이언트에서 처리해야함
            // 2. 이후 서버 로직 확인 가능함
        userChatroomService.save(userId, saveChatroom.getId());

        return new CreateChatroomResDto(saveChatroom.getId());
    }

    /**
     * 전체 채팅방 조회
     */
    @Transactional(readOnly = true)
    public List<ChatroomResDto> getChatroomList(Long userId) {
        return chatroomRepository.findAllChatroomByUserId(userId);
    }

    /**
     * 채팅 삭제
     */
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
