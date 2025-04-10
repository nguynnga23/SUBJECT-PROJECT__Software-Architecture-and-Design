package com.notificationservice.controller;

import com.notificationservice.entity.Notification;
import com.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/{userId}")
    public List<Notification> getAllForUser(@PathVariable String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @PostMapping("/")
    public Notification save(@RequestBody Notification notification) {
        notification.setCreatedAt(new Date());
        notification.setRead(false);
        return notificationRepository.save(notification);
    }
}

