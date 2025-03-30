package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.Inventory;

import java.util.UUID;

public interface InventoryService {
    boolean addBookToInventory(UUID bookId);
    int updateBookQuantity(UUID bookId, Integer quantity);
    Inventory getInventory(UUID bookId);
    boolean removeBookFromInventory(UUID bookId);
}
