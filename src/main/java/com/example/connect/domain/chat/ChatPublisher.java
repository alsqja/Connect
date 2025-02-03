package com.example.connect.domain.chat;

import com.example.connect.domain.chat.dto.ChatRabbimqRestDto;
import com.example.connect.domain.chat.dto.ChatResDto;
import com.example.connect.global.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ChatRabbimqRestDto publishChat(ChatRabbimqRestDto chatDto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_FOR_CHAT, routingKeyMapping(chatDto.getChatrommId()), chatDto);
        return chatDto;
    }

    private String routingKeyMapping(Long roomId){
        return RabbitMQConfig.TOPIC_EXCHANGE_FOR_CHAT + String.valueOf(roomId);
    }
}
