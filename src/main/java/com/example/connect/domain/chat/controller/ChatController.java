package com.example.connect.domain.chat.controller;

import com.example.connect.domain.chat.ChatPublisher;
import com.example.connect.domain.chat.dto.ChatRabbimqRestDto;
import com.example.connect.domain.chat.dto.ChatReqDto;
import com.example.connect.domain.chat.dto.ChatResDto;
import com.example.connect.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatPublisher chatPublisher;

    @MessageMapping("/chats/room/{roomId}")
    public void message(
            @DestinationVariable Long roomId,
            @RequestBody ChatReqDto message
    ) {
        LocalDateTime current = LocalDateTime.now();
        chatService.save(roomId, message, current);
        ChatRabbimqRestDto newChatDto = new ChatRabbimqRestDto(roomId, message, current);

        chatPublisher.publishChat(newChatDto);
    }
}
