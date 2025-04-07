package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.Inventory;

import java.util.UUID;

public interface InventoryService {
    Inventory saveInventory(Inventory inventory);
    Inventory getInventoryByBookId(UUID bookId);
}
