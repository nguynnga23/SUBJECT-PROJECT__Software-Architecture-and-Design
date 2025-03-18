package com.librarymanagementsystem.observers.impl;

import com.librarymanagementsystem.observers.NotificationObserver;
import com.librarymanagementsystem.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Slf4j
public class DatabaseNotificationObserver implements NotificationObserver {

    @Autowired
    private NotificationService notificationService;

    @Override
    public void update(UUID userId, String message, String type) {
        // Save the notification to the database
        notificationService.saveNotification(userId, message, type);
        log.info("Saved notification to database for user {}: {}", userId, message);
    }
}