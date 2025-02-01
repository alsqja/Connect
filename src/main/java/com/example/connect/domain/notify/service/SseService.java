package com.example.connect.domain.notify.service;

import com.example.connect.domain.notify.entity.Notify;
import com.example.connect.domain.notify.repository.EmitterRepository;
import com.example.connect.domain.notify.repository.NotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SseService {

    private final EmitterRepository emitterRepository;
    private final NotifyRepository notifyRepository;

    public SseEmitter subscribe(Long userId) {

        SseEmitter emitter = new SseEmitter(1000L * 60L * 10L);
        emitterRepository.save(userId, emitter);

        try {
            emitter.send(SseEmitter.event().name("connect").data("Connected"));
        } catch (Exception e) {
            emitter.completeWithError(e);
            emitterRepository.remove(userId);
            return emitter;
        }

        emitter.onCompletion(() -> {
            emitterRepository.remove(userId);
            emitter.complete();
        });

        emitter.onTimeout(() -> {
            emitterRepository.remove(userId);
            emitter.complete();
        });

        emitter.onError((ex) -> {
            emitterRepository.remove(userId);
            emitter.completeWithError(ex);
        });

        CompletableFuture.runAsync(() -> sendUnreadNotifications(userId, emitter));

        return emitter;
    }

    private void sendUnreadNotifications(Long userId, SseEmitter emitter) {

        try {
            List<Notify> unreadList = notifyRepository.findAllByReceiverIdAndIsReadFalse(userId);

            for (Notify notify : unreadList) {
                Map<String, Object> payload = new HashMap<>();
                payload.put("id", notify.getId());
                payload.put("type", notify.getType());
                payload.put("content", notify.getContent());
                payload.put("url", notify.getUrl());
                payload.put("createdAt", notify.getCreatedAt());

                emitter.send(SseEmitter.event().name("notify").data(payload));
            }
        } catch (Exception ex) {
            emitter.completeWithError(ex);
            emitterRepository.remove(userId);
        }
    }
}
