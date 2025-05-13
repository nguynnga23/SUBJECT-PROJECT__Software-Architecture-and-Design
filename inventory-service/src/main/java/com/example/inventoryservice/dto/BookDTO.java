package com.example.inventoryservice.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BookDTO {
    private UUID id;

    private String bookCode;

    private String title;

    private String topic;

    private String description;

    private String note;

    public BookDTO(UUID id, String bookCode, String title, String topic, String description, String note) {
        this.id = id;
        this.bookCode = bookCode;
        this.title = title;
        this.topic = topic;
        this.description = description;
        this.note = note;
    }

    public BookDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
