package com.librarymanagementsystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "event_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventLog {
    @Id
    private UUID eventId;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "event_data", columnDefinition = "jsonb")
    private String eventData;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
