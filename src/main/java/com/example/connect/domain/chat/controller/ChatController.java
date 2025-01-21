package com.example.connect.domain.chat.controller;

import com.example.connect.domain.chat.dto.ChatRequestDto;
import com.example.connect.domain.chat.dto.ChatResponseDto;
import com.example.connect.domain.chat.service.ChatService;
import com.example.connect.domain.chat.service.UserChatroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final UserChatroomService userChatroomService;
    private final ChatService chatService;

    private Long chatroomId;

    @SubscribeMapping("/search-chatroom")
    public Long createChatroom() {
        return chatroomId;
    }

    @SubscribeMapping("/sub/chatroom/{chatroomId}/history")
    public Page<ChatResponseDto> getChatHistory(@DestinationVariable Long chatroomId) {
        return userChatroomService.getChatHistory(chatroomId);
    }

    @MessageMapping("/chat")
    public void message(@RequestBody ChatRequestDto message) {

        chatService.save(message);

        simpMessageSendingOperations.convertAndSend(
                "/sub/chatroom/" + message.getChatroomId(),
                message
        );
    }
}
