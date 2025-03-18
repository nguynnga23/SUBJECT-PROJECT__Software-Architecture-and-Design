package com.librarymanagementsystem.observers.impl;

import com.librarymanagementsystem.observers.NotificationObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Slf4j
public class LoggingNotificationObserver implements NotificationObserver {

    @Override
    public void update(UUID userId, String message, String type) {
        // Log the notification for debugging purposes
        log.info("Logged notification - User: {}, Type: {}, Message: {}", userId, type, message);
    }
}