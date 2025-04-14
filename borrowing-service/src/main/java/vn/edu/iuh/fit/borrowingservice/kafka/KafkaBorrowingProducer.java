package vn.edu.iuh.fit.borrowingservice.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaBorrowingProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public KafkaBorrowingProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${spring.kafka.topic.borrowing}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendBorrowingEvent(String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Sent message to topic [" + topic + "]: " + message);
    }
}
