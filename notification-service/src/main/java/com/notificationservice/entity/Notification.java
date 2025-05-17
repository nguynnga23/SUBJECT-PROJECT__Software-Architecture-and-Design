package com.notificationservice.entity;

import com.notificationservice.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Document(collection = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    private String id;
    private UUID userId;
    private String title;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private Date createdAt;

    private Map<String, Object> metadata;
}
