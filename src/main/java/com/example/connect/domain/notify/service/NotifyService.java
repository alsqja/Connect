package com.example.connect.domain.notify.service;

import com.example.connect.domain.notify.entity.Notify;
import com.example.connect.domain.notify.repository.EmitterRepository;
import com.example.connect.domain.notify.repository.NotifyRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.enums.NotifyType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotifyService {

    private final NotifyRepository notifyRepository;
    private final EmitterRepository emitterRepository;

    public void sendNotify(User receiver, NotifyType notifyType, String content, String url) {

        System.out.println(receiver.getId());
        Notify notify = new Notify(notifyType, content, receiver, url);
        notifyRepository.save(notify);

        SseEmitter emitter = emitterRepository.get(receiver.getId());
        if (emitter != null) {
            try {
                Map<String, Object> payload = new HashMap<>();
                payload.put("type", notifyType);
                payload.put("content", content);
                payload.put("url", url);

                emitter.send(
                        SseEmitter.event()
                                .name("notify")
                                .data(payload)
                );
            } catch (Exception e) {
                emitterRepository.remove(receiver.getId());
            }
        }
    }

    public void markAsRead(Long notifyId) {

        Notify notify = notifyRepository.findById(notifyId).orElse(null);

        if (notify != null && !notify.isRead()) {
            notify.updateRead();
            notifyRepository.save(notify);
        }
    }

    @Transactional
    public void markAllAsRead(Long userId) {

        notifyRepository.markAllAsRead(userId);
    }
}
