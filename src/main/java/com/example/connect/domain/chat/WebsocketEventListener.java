package com.example.connect.domain.chat;

import com.example.connect.domain.chat.dto.ChatRequestDto;
import com.example.connect.domain.chat.dto.ChatResponseDto;
import com.example.connect.domain.chat.service.ChatroomService;
import com.example.connect.domain.chat.service.UserChatroomService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebsocketEventListener {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatroomService chatroomService;
    private final UserChatroomService userChatroomService;

    // 웹소캣 세션 연결 시
    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        // 헤더 값 조회
        Long chatroomId = parseHeaderAsLong(headerAccessor, "chatroomId");
        Long matchingId = parseHeaderAsLong(headerAccessor, "matchingId");

        Authentication authentication = (Authentication) headerAccessor.getUser();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto redisUserDto = userDetails.getUser();

        if (chatroomId == null) {
            // 새 채팅방 생성
            chatroomId = chatroomService.create(matchingId);

            // TODO: Websocker 프로토콜에서도 정상적으로 토큰을 받아올 수 있는지 확인 필요, 없으면 header로 넘기기
            // 사용자 인증 정보 가져오기

            userChatroomService.save(redisUserDto.getId(), chatroomId);

            // 생성된 채팅방 id 클라이언트로 전송
            simpMessageSendingOperations.convertAndSend(
                    "/search-chatroom",
                    chatroomId
            );
        } else {
            // 기존 채팅방의 채팅 내역 불러오기
            Page<ChatResponseDto> chatHistory = userChatroomService.getChatHistory(chatroomId);

            // 조회한 채팅 내역
            simpMessageSendingOperations.convertAndSend(
                    "/topic/chatroom/" + chatroomId + "/history",
                    chatHistory
            );
        }
        log.info("[ {} ][ 세션 연결 ] - chatroom: {}, user: {}", sessionId, chatroomId, redisUserDto.getId());
    }

    private Long parseHeaderAsLong(StompHeaderAccessor headerAccessor, String headerName) {
        String headerValue = headerAccessor.getFirstNativeHeader(headerName);
        if (headerValue == null) {
            return null;
        }
        try {
            return Long.valueOf(headerValue);
        } catch (NumberFormatException e) {
            log.error("유효하지 않은 Header 값 {}: {}", headerName, headerValue);
            return null;
        }
    }
}
