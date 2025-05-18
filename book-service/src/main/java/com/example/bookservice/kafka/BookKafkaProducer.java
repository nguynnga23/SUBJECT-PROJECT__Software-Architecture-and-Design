package com.example.bookservice.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public BookKafkaProducer(KafkaTemplate<String, String> kafkaTemplate,
                             @Value("${spring.kafka.topic.book}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendBookCreatedEvent(String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Sent BookCreated event: " + message);
    }
}
