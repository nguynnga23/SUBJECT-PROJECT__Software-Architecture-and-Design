package com.example.bookservice.dto;

import java.util.Set;
import java.util.UUID;

public record BookRequestDTO(
        String bookCode,
        String title,
        String topic,
        String description,
        String note,
        UUID categoryId,
        int yearPublished,
        String publisher,
        Set<UUID> authorIds
) {
}
