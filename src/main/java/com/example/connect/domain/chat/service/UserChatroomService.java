package com.example.connect.domain.chat.service;

import com.example.connect.domain.chat.dto.ChatResDto;
import com.example.connect.domain.chat.entity.Chatroom;
import com.example.connect.domain.chat.entity.UserChatroom;
import com.example.connect.domain.chat.repository.ChatroomRepository;
import com.example.connect.domain.chat.repository.UserChatroomRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserChatroomService {

    private final UserChatroomRepository userChatroomRepository;
    private final ChatroomRepository chatroomRepository;
    private final UserRepository userRepository;

    /**
     * 채팅 내역 조회
     */
    @Transactional(readOnly = true)
    public List<ChatResDto> getChatHistory(Long userId, Long chatroomId) {

        // 유저 인증
        userChatroomRepository.findByUserIdAndChatroomIdOrElseThrow(userId, chatroomId);


        // TODO: 페이징 처리 고려, 프론트에서 스크롤 이벤트로 다음 페이지 받아오기
        // Pageable 설정 (최대 15개 조회)
        // Pageable pageable = PageRequest.of(0, 15);

        return userChatroomRepository.findTopChatsByChatroomId(chatroomId) ;
    }

    @Transactional
    public void save(Long userId, Long chatroomId) {
        User findUser = userRepository.findByIdOrElseThrow(userId);
        Chatroom findChatroom = chatroomRepository.findByIdOrElseThrow(chatroomId);
        UserChatroom userChatroom = new UserChatroom(findUser, findChatroom);
        userChatroomRepository.save(userChatroom);
    }
}
