package com.example.inventoryservice.dto;

import com.example.inventoryservice.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class BookCopyDTO {
    private UUID id;
    private BookDTO book;
    private String copyCode;
    private String location;
    private String status; // "AVAILABLE", "BORROWED", etc.
}

