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
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new EntityNotFoundException("BookCopy not found with ID: " + bookCopyId));

        Inventory inventory = inventoryRepository.getInventoryByBookId(bookCopy.getBookId());
        if (inventory == null) {
            throw new EntityNotFoundException("Inventory not found for bookId: " + bookCopy.getBookId());
        }

        // Chỉ cập nhật trạng thái của BookCopy
        if (newInventory.getBorrowed() != null && newInventory.getBorrowed() > 0) {
            bookCopy.setStatus(Status.BORROWED);
        } else if (newInventory.getDamaged() != null && newInventory.getDamaged() > 0) {
            bookCopy.setStatus(Status.DAMAGED);
        } else if (newInventory.getLost() != null && newInventory.getLost() > 0) {
            bookCopy.setStatus(Status.LOST);
        } else {
            bookCopy.setStatus(Status.AVAILABLE);
        }

        // Lưu BookCopy sau khi cập nhật
        bookCopyRepository.save(bookCopy);
        return inventoryRepository.getInventoryByBookId(bookCopy.getBookId()); // Lấy lại Inventory đã cập nhật
    }


    @Override
    public Inventory updateActionInventory(UUID bookId, Status status) {
        Inventory inventory = inventoryRepository.getInventoryByBookId(bookId);

        if (inventory == null) {
            throw new IllegalArgumentException("Inventory not found for bookId: " + bookId);
        }

        // Lấy danh sách bản sao của sách
        BookCopy bookCopy = inventory.getBookCopies().stream()
                .filter(copy -> copy.getStatus().name().equalsIgnoreCase(Status.AVAILABLE.name())
                        || (status == Status.AVAILABLE && copy.getStatus().name().equalsIgnoreCase(Status.BORROWED.name())))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No suitable book copy found for the action."));

        switch (status) {
            case BORROWED:
                bookCopy.setStatus(Status.BORROWED);
                break;
            case AVAILABLE: // Trả sách
                bookCopy.setStatus(Status.AVAILABLE);
                break;
            case LOST:
                bookCopy.setStatus(Status.LOST);
                break;
            case DAMAGED:
                bookCopy.setStatus(Status.DAMAGED);
                break;
        }

        bookCopyRepository.save(bookCopy); // Cập nhật book copy
        return inventory; // Không cần save lại inventory vì bookCopies đã được cập nhật
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
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new EntityNotFoundException("BookCopy not found with ID: " + bookCopyId));

        if (!bookCopy.getStatus().name().equalsIgnoreCase(Status.AVAILABLE.name())) {
            throw new IllegalArgumentException("BookCopy is not available for borrowing.");
        }

        // Cập nhật trạng thái của BookCopy thành BORROWED
        bookCopy.setStatus(Status.BORROWED);
        bookCopyRepository.save(bookCopy);
    }

}
