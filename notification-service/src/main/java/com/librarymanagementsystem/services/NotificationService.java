package com.librarymanagementsystem.services;

import com.librarymanagementsystem.entities.EventLog;
import com.librarymanagementsystem.entities.Notification;
import com.librarymanagementsystem.entities.Template;
import com.librarymanagementsystem.observers.NotificationSubject;
import com.librarymanagementsystem.repositories.EventLogRepository;
import com.librarymanagementsystem.repositories.NotificationRepository;
import com.librarymanagementsystem.repositories.TemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class NotificationService extends NotificationSubject {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EventLogRepository eventRepository;

    @Autowired
    private TemplateRepository templateRepository;

    public void saveEvent(EventLog event) {
        eventRepository.save(event);
    }

    public void createNotification(UUID userId, String type, Map<String, Object> data) {
        // Find the template for the given type
        Template template = templateRepository.findByType(type)
                .orElseThrow(() -> new RuntimeException("Template not found for type: " + type));

        // Replace placeholders in the template content with actual data
        String message = template.getContent();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }

        // Notify all observers (e.g., save to DB, send email, etc.)
        notifyObservers(userId, message, type);

        log.info("Notification created for user {}: {}", userId, message);
    }

    // Method to create and save a notification directly (if needed)
    public Notification saveNotification(UUID userId, String message, String type) {
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID());
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }
}