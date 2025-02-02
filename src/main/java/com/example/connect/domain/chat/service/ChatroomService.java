package com.example.connect.domain.chat.service;

import com.example.connect.domain.chat.dto.ChatroomResDto;
import com.example.connect.domain.chat.dto.CreateChatroomResDto;
import com.example.connect.domain.chat.entity.Chatroom;
import com.example.connect.domain.chat.entity.UserChatroom;
import com.example.connect.domain.chat.entity.enums.RoomStatus;
import com.example.connect.domain.chat.repository.ChatRepository;
import com.example.connect.domain.chat.repository.ChatroomRepository;
import com.example.connect.domain.chat.repository.UserChatroomRepository;
import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.match.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatRepository chatRepository;
    private final ChatroomRepository chatroomRepository;
    private final UserChatroomService userChatroomService;
    private final UserChatroomRepository userChatroomRepository;
    private final MatchingRepository matchingRepository;

    /**
     * 채팅방 생성
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
        userChatroomService.save(findMatching.getFromSchedule().getUser().getId(), saveChatroom.getId());

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
    public void leaveChatroom(Long userId, Long roomId) {
        // 1. 유저가 채팅방을 떠남 (isDelete = true 로 변경)
        UserChatroom userChatroom = userChatroomRepository.findByUserIdAndChatroomIdAndIsDeleteFalseOrElseThrow(userId, roomId);
        userChatroom.markAsDeleted();

        // 2. 남아있는 유저가 있는지 확인
        boolean hasActiveUsers = userChatroomRepository.existsByChatroomIdAndIsDeleteFalse(roomId);

        // 3. 남아있는 유저가 없으면 채팅방 및 채팅 메시지 삭제
        if (!hasActiveUsers) {
            deleteAllAboutChatAndChatroom(roomId);
        }
    }

    // 채팅 내역 및 채팅방 삭제
    private void deleteAllAboutChatAndChatroom(Long chatroomId) {
        // 1. 중간 테이블 삭제
        userChatroomRepository.deleteAllByChatroomId(chatroomId);

        // 2. 채팅방의 채팅 내역 삭제
        chatRepository.deleteAllByChatroomId(chatroomId);

        // 2. 채팅방 삭제
        chatroomRepository.deleteById(chatroomId);
    }
}
