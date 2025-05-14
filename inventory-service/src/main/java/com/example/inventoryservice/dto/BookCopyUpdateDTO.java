package com.example.inventoryservice.dto;

import com.example.inventoryservice.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCopyUpdateDTO {
    private String location;
    private Status status; // "AVAILABLE", "BORROWED", etc.
}
