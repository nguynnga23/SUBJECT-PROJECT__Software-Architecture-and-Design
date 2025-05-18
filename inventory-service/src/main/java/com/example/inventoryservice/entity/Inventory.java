package com.example.inventoryservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "book_id")
    private UUID bookId;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @OneToMany(mappedBy = "inventory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<BookCopy> bookCopies;

    public Integer getAvailable() {
        return (int) bookCopies.stream().filter(copy -> copy.getStatus().name().equalsIgnoreCase("AVAILABLE")).count();
    }

    public Integer getBorrowed() {
        return (int) bookCopies.stream().filter(copy -> copy.getStatus().name().equalsIgnoreCase("BORROWED")).count();
    }

    public Integer getLost() {
        return (int) bookCopies.stream().filter(copy -> copy.getStatus().name().equalsIgnoreCase("LOST")).count();
    }

    public Integer getDamaged() {
        return (int) bookCopies.stream().filter(copy -> copy.getStatus().name().equalsIgnoreCase("DAMAGED")).count();
    }

    // Method to calculate totalQuantity based on bookCopies size
    @PostLoad
    @PostPersist
    @PostUpdate
    private void updateTotalQuantity() {
        if (this.bookCopies != null) {
            this.totalQuantity = this.bookCopies.size();
        }
    }
}
