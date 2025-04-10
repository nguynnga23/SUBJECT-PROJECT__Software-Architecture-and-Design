package com.notificationservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {
    @KafkaListener(
            topics = "${spring.kafka.topic.borrowing}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeMessage(String message) {
        System.out.println("Received message: " + message);
    }
}