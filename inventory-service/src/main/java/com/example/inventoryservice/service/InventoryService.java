package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.BookCopyRequest;
import com.example.inventoryservice.dto.BookDTO;
import com.example.inventoryservice.entity.Inventory;

import java.util.UUID;

public interface InventoryService {
    BookDTO getBookById(UUID bookId);
    Inventory addBookToInventory(BookCopyRequest bookCopyRequest) throws Exception;
    Inventory updateBookQuantity(UUID bookId, String action);
    Inventory getInventory(UUID bookId);
    boolean removeBookFromInventory(UUID bookId);
}
