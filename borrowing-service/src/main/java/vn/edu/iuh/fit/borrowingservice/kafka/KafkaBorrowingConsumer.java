package vn.edu.iuh.fit.borrowingservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaBorrowingConsumer {
    @KafkaListener(
            topics = "${spring.kafka.topic.borrowing-inventory}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeMessage(String message) {
        System.out.println("Received message: " + message);
        // Xử lý logic khi nhận được message
    }
}
