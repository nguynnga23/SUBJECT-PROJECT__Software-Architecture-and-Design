package com.example.inventoryservice.service.impl;
import com.example.inventoryservice.entity.BookCopy;
import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.enums.Status;
import com.example.inventoryservice.repositories.BookCopyRepository;
import com.example.inventoryservice.repositories.InventoryRepository;
import com.example.inventoryservice.service.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryServiceImpl implements InventoryService{
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Override
    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory getInventoryByBookId(UUID bookId) {
        return inventoryRepository.getInventoryByBookId(bookId);
    }

    @Override
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public boolean deleteInventory(UUID bookId) {
        if (inventoryRepository.existsById(bookId)) {
            inventoryRepository.deleteById(bookId);
            return true;
        }
        return false;
    }

    @Override
    public Inventory updateInventory(UUID bookCopyId, Inventory newInventory) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId).orElseThrow(EntityNotFoundException::new);
        Inventory inventory = inventoryRepository.getInventoryByBookId(bookCopy.getBookId());
        if (inventory == null) {
            throw new EntityNotFoundException("Inventory not found for bookId: " + bookCopy.getBookId());
        }
        if (newInventory.getBorrowed() != null) {
            inventory.setBorrowed(newInventory.getBorrowed());
        }
        if (newInventory.getDamaged() != null) {
            inventory.setDamaged(newInventory.getDamaged());
        }
        if (newInventory.getAvailable() != null) {
            inventory.setAvailable(newInventory.getAvailable());
        }
        if (newInventory.getLost() != null) {
            inventory.setLost(newInventory.getLost());
        }
        if (newInventory.getTotalQuantity() != null) {
            inventory.setTotalQuantity(newInventory.getTotalQuantity());
        }
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateActionInventory(UUID bookId, Status status) {
        Inventory inventory = inventoryRepository.getInventoryByBookId(bookId);
        switch (status){
            case BORROWED:
                inventory.setAvailable(inventory.getAvailable() - 1);
                inventory.setBorrowed(inventory.getBorrowed() + 1);
                break;
            case AVAILABLE: // Tra sach
                inventory.setAvailable(inventory.getAvailable() + 1);
                inventory.setBorrowed(inventory.getBorrowed() - 1);
                break;
            case LOST:
                inventory.setLost(inventory.getLost() + 1);
                inventory.setBorrowed(inventory.getBorrowed() - 1);
                break;
            case DAMAGED:
                inventory.setDamaged(inventory.getDamaged() + 1);
                inventory.setBorrowed(inventory.getBorrowed() - 1);
                break;
        }
        return inventoryRepository.save(inventory);
    }

    @Override
    public boolean checkAvailableQuantity(UUID bookId) {
        Inventory inventory = inventoryRepository.getInventoryByBookId(bookId);
        if (inventory == null) {
            throw new EntityNotFoundException("Inventory not found for bookId: " + bookId);
        }
        return inventory.getAvailable() > 0;
    }

    @Override
    public void updateBookAvailability(UUID bookCopyId) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId).orElseThrow(EntityNotFoundException::new);
        Inventory inventory = inventoryRepository.getInventoryByBookId(bookCopy.getBookId());
        if (inventory == null) {
            throw new EntityNotFoundException("Inventory not found for bookId: " + bookCopy.getBookId());
        }
        inventory.setAvailable(inventory.getAvailable() - 1);
        inventory.setBorrowed(inventory.getBorrowed() + 1);
        inventoryRepository.save(inventory);
    }
}
