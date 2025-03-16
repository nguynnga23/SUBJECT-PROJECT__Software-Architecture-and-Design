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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String eventType; // ORDER_PLACED, LOW_INVENTORY
    @Column(columnDefinition = "JSONB")
    private String payload;
    private boolean processed;
    private LocalDateTime createdAt;
}
