package com.notificationservice.seed;
import com.notificationservice.entity.Notification;
import com.notificationservice.enums.NotificationType;
import com.notificationservice.repository.NotificationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    private final NotificationRepository notificationRepository;

    public DataSeeder(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void run(String... args) {
//        Notification notification = new Notification();
//        notification.setUserId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
//        notification.setMessage("Sách bạn mượn sắp đến hạn trả!");
//        notification.setType(NotificationType.DUE_DATE_REMINDER);
//        notification.setRead(false);
//        notification.setCreatedAt(Date.from(Instant.now()));
//        notificationRepository.save(notification);
    }
}
