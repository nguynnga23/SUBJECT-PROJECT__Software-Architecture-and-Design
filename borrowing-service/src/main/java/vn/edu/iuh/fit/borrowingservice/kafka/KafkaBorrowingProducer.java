package vn.edu.iuh.fit.borrowingservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaBorrowingProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.borrowing-notification}")
    private String notificationTopic;

    @Value("${spring.kafka.topic.borrowing-inventory}")
    private String inventoryTopic;

    public KafkaBorrowingProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToNotificationService(String message) {
        kafkaTemplate.send(notificationTopic, message);
        System.out.println("Sent to Notification topic: " + message);
    }

    public void sendToInventoryService(String message) {
        kafkaTemplate.send(inventoryTopic, message);
        System.out.println("Sent to Inventory topic: " + message);
    }
}

