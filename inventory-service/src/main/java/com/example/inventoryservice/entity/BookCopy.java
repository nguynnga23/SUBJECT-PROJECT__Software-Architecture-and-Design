package com.example.inventoryservice.entity;

import com.example.inventoryservice.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "book_copy")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookCopy {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "book_id")
    private UUID bookId;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    @JsonBackReference
    private Inventory inventory;

    @Column(name = "copy_code", unique = true, nullable = false)
    private String copyCode;

    private String location;

    @Enumerated(EnumType.STRING)
    private Status status;
}
