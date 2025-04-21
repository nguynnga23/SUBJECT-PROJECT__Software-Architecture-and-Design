package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.enums.Status;

import java.util.List;
import java.util.UUID;

public interface InventoryService {
    Inventory saveInventory(Inventory inventory);
    Inventory getInventoryByBookId(UUID bookId);
    List<Inventory> getAllInventory();
    boolean deleteInventory(UUID bookId);
    Inventory updateInventory(UUID bookId, Inventory inventory);
    Inventory updateActionInventory(UUID bookId, Status status);
}
