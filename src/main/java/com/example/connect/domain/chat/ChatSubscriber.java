package com.example.connect.domain.chat;

import com.example.connect.domain.chat.dto.ChatRabbimqRestDto;
import com.example.connect.global.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatSubscriber {

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = RabbitMQConfig.CHAT_QUEUE)
    public void javaNews(ChatRabbimqRestDto chat) {
        messagingTemplate.convertAndSend("/sub/chats/room/" + chat.getChatrommId(), chat);
    }
}
