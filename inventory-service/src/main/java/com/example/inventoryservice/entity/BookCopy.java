package com.example.inventoryservice.entity;

import com.example.inventoryservice.enums.Status;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "book_copy")
public class BookCopy {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "book_id")
    private UUID bookId;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    private String copyCode;

    private String location;

    @Enumerated(EnumType.STRING)
    private Status status;
}
