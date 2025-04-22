package com.example.inventoryservice.dto;

import java.util.UUID;

public class BookCopyRequestDTO {
    private UUID bookId;
    private int quantity;
    private String location;

    // Getters & Setters
    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
