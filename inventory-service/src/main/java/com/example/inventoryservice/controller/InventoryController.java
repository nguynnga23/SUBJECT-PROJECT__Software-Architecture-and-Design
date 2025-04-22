package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.BookCopyRequestDTO;
import com.example.inventoryservice.dto.BookDTO;
import com.example.inventoryservice.entity.BookCopy;
import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.enums.Status;
import com.example.inventoryservice.service.BookCopyService;
import com.example.inventoryservice.service.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Book;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory-service/inventories")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BookCopyService bookCopyService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookCopy(@PathVariable UUID bookId) {
        Inventory inventory = inventoryService.getInventoryByBookId(bookId);
        if(inventory != null){
            return ResponseEntity.ok(inventory);
        }
        return ResponseEntity.status(400).body(Map.of("error","Book does not exist"));
    }

    @DeleteMapping("/remove/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable UUID bookId){
        boolean isDeleted = inventoryService.deleteInventory(bookId);

        if(isDeleted){
            return ResponseEntity.ok(Map.of("message", "Inventory deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "BookId not found"));
        }
    }

    @PutMapping("/update/{bookId}")
    public ResponseEntity<?> updateInventory(@PathVariable UUID bookId, @RequestBody Inventory newInventory){
        Inventory inventory = inventoryService.getInventoryByBookId(bookId);
        if(inventory != null){
            inventoryService.updateInventory(bookId, newInventory);
            return ResponseEntity.ok(inventory);
        }
        return ResponseEntity.ok(Map.of("error", "Inventory not found for bookId"));
    }
    @PutMapping("/update-action/{action}/{bookId}")
    public ResponseEntity<?> updateActionInventory(@PathVariable Status action, @PathVariable UUID bookId){
        Inventory inventory = inventoryService.getInventoryByBookId(bookId);
        if(inventory != null){
            inventoryService.updateActionInventory(bookId, action);
            return ResponseEntity.ok(inventory);
        }
        return ResponseEntity.ok(Map.of("error", "Inventory not found for bookId"));
    }

    @GetMapping
    public ResponseEntity<?> getAllInventories() {
        try {
            return ResponseEntity.ok(inventoryService.getAllInventory());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve inventories"));
        }
    }

    @GetMapping("/check-available/{bookId}")
    public ResponseEntity<?> checkAvailable(@PathVariable UUID bookId) {
        try {
            Inventory inventory = inventoryService.getInventoryByBookId(bookId);
            if (inventory == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Inventory not found for bookId: " + bookId));
            }

            int available = inventory.getAvailable();
            return ResponseEntity.ok(Map.of(
                    "available", available > 0,
                    "quantity", available
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to check availability"));
        }
    }


//    @GetMapping("/inventory/{id}")
//    public ResponseEntity<?> getInventoryById(@PathVariable UUID id) {
//        try {
//            Inventory inventory = inventoryService.getInventoryByBookId(id);
//            if (inventory != null) {
//                return ResponseEntity.ok(inventory);
//            }
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Map.of("error", "Inventory not found for ID: " + id));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Failed to retrieve inventory"));
//        }
//    }

}
