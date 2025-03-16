package com.librarymanagementsystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String recipient;
    private String type; // EMAIL, SMS, PUSH
    private UUID templateId;
    private String status; // PENDING, SENT, FAILED
    private LocalDateTime scheduledTime;
    private LocalDateTime createdAt;
}
