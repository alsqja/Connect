package com.example.connect.domain.chat;

import com.example.connect.domain.chat.dto.ChatRabbimqRestDto;
import com.example.connect.global.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ChatRabbimqRestDto publishChat(ChatRabbimqRestDto chatDto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.CHAT_TOPIC_EXCHANGE, routingKeyMapping(chatDto.getChatroomId()), chatDto);
        return chatDto;
    }

    private String routingKeyMapping(Long roomId){
        return RabbitMQConfig.CHAT_TOPIC_PREFIX + String.valueOf(roomId);
    }
}
