package com.example.connect.domain.chat;

import com.example.connect.domain.chat.dto.ChatRabbimqRestDto;
import com.example.connect.global.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatSubscriber {

    private int retryCount;

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = RabbitMQConfig.CHAT_QUEUE)
    public void javaNews(ChatRabbimqRestDto chat) {
        log.warn("Received message: {}, count : {}", chat.getMessage(), retryCount++);

        messagingTemplate.convertAndSend("/sub/chats/room/" + chat.getChatroomId(), chat);
    }
}
