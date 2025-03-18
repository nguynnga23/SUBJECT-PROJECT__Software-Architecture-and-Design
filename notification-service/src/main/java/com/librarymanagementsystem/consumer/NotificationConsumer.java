package com.librarymanagementsystem.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.librarymanagementsystem.entities.EventLog;
import com.librarymanagementsystem.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class NotificationConsumer {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "borrowing-events", groupId = "notification-service-group")
    public void consumeBorrowingEvent(String message) {
        try {
            // Parse the Kafka message (assumed to be JSON)
            Map<String, Object> eventData = objectMapper.readValue(message, HashMap.class);
            String eventType = (String) eventData.get("event_type");
            String userId = (String) eventData.get("user_id");
            String bookId = (String) eventData.get("book_id");

            // Log the event in NotificationEvent
            EventLog event = new EventLog();
            event.setEventId(UUID.randomUUID());
            event.setEventType(eventType);
            event.setEventData(message);
            event.setCreatedAt(LocalDateTime.now());
            notificationService.saveEvent(event);

            // Generate and save a notification based on the event type
            switch (eventType) {
                case "borrow_request":
                    notificationService.createNotification(
                            UUID.fromString(userId),
                            "BORROW_CONFIRMATION",
                            Map.of("bookId", bookId)
                    );
                    break;
                case "return_request":
                    notificationService.createNotification(
                            UUID.fromString(userId),
                            "RETURN_CONFIRMATION",
                            Map.of("bookId", bookId)
                    );
                    break;
                case "overdue_warning":
                    notificationService.createNotification(
                            UUID.fromString(userId),
                            "OVERDUE_WARNING",
                            Map.of("bookId", bookId)
                    );
                    break;
                default:
                    log.warn("Unknown event type: {}", eventType);
            }
        } catch (Exception e) {
            log.error("Error processing Kafka message: {}", message, e);
        }
    }
}