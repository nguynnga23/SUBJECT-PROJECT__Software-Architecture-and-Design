package com.notificationservice.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consumeMessage(String message) {
        System.out.println("Received message: " + message);
        // Xử lý logic khi nhận được tin nhắn
    }
}