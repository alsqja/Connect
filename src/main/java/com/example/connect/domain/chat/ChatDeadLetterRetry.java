package com.example.connect.domain.chat;

import com.example.connect.domain.chat.dto.ChatRabbimqRestDto;
import com.example.connect.global.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 데드레터로 들어온 메세지를 Requeue 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatDeadLetterRetry {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues =  RabbitMQConfig.DLQ)
    public void processDlqMessage(ChatRabbimqRestDto chat) {
        try {
            log.warn("[DLQ Received]: {}",  chat.getMessage());

            // 수정된 메세지를 원래 큐로 재전송
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.CHAT_TOPIC_EXCHANGE,
                    RabbitMQConfig.CHAT_TOPIC_PREFIX + chat.getChatroomId(),
                    chat
            );

            log.info("Message successfully reprocessed : {}", chat.getMessage());

        } catch (Exception e) {
            log.error("Error processing DLQ message: {}", e);
        }
    }

}
