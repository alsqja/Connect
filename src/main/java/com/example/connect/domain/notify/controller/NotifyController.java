package com.example.connect.domain.notify.controller;

import com.example.connect.domain.notify.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifies")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;

    @PostMapping("/read-all/{userId}")
    public ResponseEntity<Void> readAll(@PathVariable Long userId) {

        notifyService.markAllAsRead(userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
