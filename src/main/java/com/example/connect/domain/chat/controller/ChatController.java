package com.example.connect.domain.chat.controller;

import com.example.connect.domain.chat.dto.ChatReqDto;
import com.example.connect.domain.chat.dto.ChatResDto;
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
    public Page<ChatResDto> getChatHistory(@DestinationVariable Long chatroomId) {
        return userChatroomService.getChatHistory(chatroomId);
    }

    @MessageMapping("/chats/room/{roomId}")
    public void message(@DestinationVariable Long roomId,  @RequestBody ChatReqDto message) {

        chatService.save(roomId, message);

        simpMessageSendingOperations.convertAndSend(
                "/sub/chats/room/" + roomId,
                message
        );
    }
}
