package com.example.inventoryservice.dto;

import java.util.UUID;

public record BookDTO (UUID id, String title, String bookCode) {
}
