package com.example.connect.domain.notify.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(Long userId, SseEmitter emitter) {
        emitters.put(userId.toString(), emitter);
        return emitter;
    }

    public SseEmitter remove(Long userId) {
        return emitters.remove(userId.toString());
    }

    public SseEmitter get(Long userId) {
        return emitters.get(userId.toString());
    }
}
