package com.librarymanagementsystem.observers.impl;

import com.librarymanagementsystem.observers.NotificationObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Slf4j
public class EmailNotificationObserver implements NotificationObserver {

    @Override
    public void update(UUID userId, String message, String type) {
        // Simulate sending an email (you can integrate with an email service here)
        log.info("Sending email to user {}: {}", userId, message);
        // Example: Use Spring Mail to send an actual email
        // emailService.sendEmail(userId.toString() + "@example.com", "Notification", message);
    }
}