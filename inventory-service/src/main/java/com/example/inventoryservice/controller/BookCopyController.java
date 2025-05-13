package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.BookCopyRequestDTO;
import com.example.inventoryservice.entity.BookCopy;
import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.enums.Status;
import com.example.inventoryservice.service.BookCopyService;
import com.example.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory-service/copies")
public class BookCopyController {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BookCopyService bookCopyService;

    @PostMapping
    public ResponseEntity<?> addBookCopy(@RequestBody BookCopyRequestDTO request) {
        UUID bookId = request.getBookId();
        int quantity = request.getQuantity();
        String location = request.getLocation();

        Inventory inventory = inventoryService.getInventoryByBookId(bookId);

        if (inventory == null) {
            Inventory newInventory = new Inventory();
            newInventory.setAvailable(quantity);
            newInventory.setTotalQuantity(quantity);
            newInventory.setLost(0);
            newInventory.setDamaged(0);
            newInventory.setBorrowed(0);
            newInventory.setBookId(bookId);
            inventory = inventoryService.saveInventory(newInventory);
        }

        for (int i = 0; i < quantity; i++) {
            BookCopy bookCopy = new BookCopy();
            String latestCode = bookCopyService.findLatestCopyCode();
            int nextNumber = 1;
            if (latestCode != null && latestCode.startsWith("BC_")) {
                nextNumber = Integer.parseInt(latestCode.substring(3)) + 1;
            }
            String formattedCode = String.format("BC_%03d", nextNumber);
            bookCopy.setCopyCode(formattedCode);
            bookCopy.setBookId(bookId);
            bookCopy.setLocation(location);
            bookCopy.setStatus(Status.AVAILABLE);
            bookCopy.setInventory(inventory);
            bookCopyService.addBookCopy(bookCopy);

            inventory.setTotalQuantity(inventory.getTotalQuantity() + 1);
            inventory.setAvailable(inventory.getAvailable() + 1);
        }

        inventoryService.saveInventory(inventory);

        return ResponseEntity.ok(Map.of("message", "Added " + quantity + " book copies successfully"));
    }

    @GetMapping
    public ResponseEntity<?> getAllBookCopies() {
        return ResponseEntity.ok(bookCopyService.getAllBookCopy());
    }

    @GetMapping("/{bookCopyId}")
    public ResponseEntity<?> getBookCopyById(@RequestParam UUID bookCopyId) {
        return ResponseEntity.ok(bookCopyService.getBookCopyById(bookCopyId));
    }

//    @PostMapping
//    public ResponseEntity<BookCopy> addBookCopy(@RequestBody BookCopy bookCopy) {
//        BookCopy savedBookCopy = bookCopyService.addBookCopy(bookCopy);
//        return ResponseEntity.ok(savedBookCopy);
//    }

    @GetMapping("/latest-copy-code")
    public ResponseEntity<String> getLatestCopyCode() {
        String latestCopyCode = bookCopyService.findLatestCopyCode();
        return ResponseEntity.ok(latestCopyCode);
    }

    @GetMapping("/available-copy/{bookId}")
    public ResponseEntity<?> getAvailableCopy(@PathVariable UUID bookId) {
        try {
            BookCopy availableCopy = bookCopyService.findFirstByBookIdAndStatus(bookId, Status.AVAILABLE);
            if (availableCopy == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No available copy found for bookId: " + bookId));
            }
            return ResponseEntity.ok(availableCopy);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch available copy"));
        }
    }
}
