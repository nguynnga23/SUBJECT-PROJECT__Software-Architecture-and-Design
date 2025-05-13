package com.example.inventoryservice.dto;

import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.UUID;

public class BookCopyDTO {
    private UUID id;


    private String copyCode;

    private String location;

    private Status status;
    private BookDTO book;

    public BookCopyDTO(UUID id, String copyCode, String location, Status status, BookDTO book) {
        this.id = id;
        this.copyCode = copyCode;
        this.location = location;
        this.status = status;
        this.book = book;
    }

    public BookCopyDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    public String getCopyCode() {
        return copyCode;
    }

    public void setCopyCode(String copyCode) {
        this.copyCode = copyCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BookCopyDTO{" +
                "id=" + id +
                ", book=" + book +
                ", copyCode='" + copyCode + '\'' +
                ", location='" + location + '\'' +
                ", status=" + status +
                '}';
    }
}
