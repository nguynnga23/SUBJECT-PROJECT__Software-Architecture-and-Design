package com.librarymanagementsystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String type; // EMAIL, SMS, PUSH
    private String name;
    @Column(columnDefinition = "TEXT")
    private String content;
}

