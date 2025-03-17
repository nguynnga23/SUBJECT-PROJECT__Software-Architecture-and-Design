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
    private UUID id;

    private String type;

    private String name;

    private String content;
}

