package com.example.inventoryservice.dto;

import com.example.inventoryservice.enums.Status;

import java.util.UUID;

public record BookCopyRequest(UUID bookId, String copyCode, String location, Status status, UUID inventoryId) {
}
