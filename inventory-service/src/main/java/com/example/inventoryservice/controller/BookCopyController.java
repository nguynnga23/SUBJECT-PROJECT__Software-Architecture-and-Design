package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.BookCopyDTO;
import com.example.inventoryservice.dto.BookCopyRequestDTO;
import com.example.inventoryservice.dto.BookCopyUpdateDTO;
import com.example.inventoryservice.entity.BookCopy;
import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.enums.Status;
import com.example.inventoryservice.mapper.BookCopyMapper;
import com.example.inventoryservice.service.BookCopyService;
import com.example.inventoryservice.service.InventoryService;
import jakarta.persistence.EntityNotFoundException;
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
    @Autowired
    private BookCopyMapper bookCopyMapper;
    @PostMapping
    public ResponseEntity<?> addBookCopy(@RequestBody BookCopyRequestDTO request) {
        UUID bookId = request.getBookId();
        int quantity = request.getQuantity();
        String location = request.getLocation();

        Inventory inventory = inventoryService.getInventoryByBookId(bookId);

        if (inventory == null) {
            inventory = new Inventory();
            inventory.setBookId(bookId);
            inventory.setTotalQuantity(0); // Ban đầu là 0, sẽ được cập nhật sau
            inventory = inventoryService.saveInventory(inventory);
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
        }

        // Cập nhật tổng số lượng
        inventory.setTotalQuantity(inventory.getBookCopies().size());
        inventoryService.saveInventory(inventory);

        return ResponseEntity.ok(Map.of("message", "Added " + quantity + " book copies successfully"));
    }


    @GetMapping
    public ResponseEntity<?> getAllBookCopies() {
        return ResponseEntity.ok(bookCopyService.getAllBookCopy());
    }

    @GetMapping("/{bookCopyId}")
    public ResponseEntity<BookCopyDTO> getBookCopyById(@PathVariable UUID bookCopyId) {
        BookCopyDTO bookCopy = bookCopyMapper.mapToDto(bookCopyService.getBookCopyById(bookCopyId));
        return ResponseEntity.ok(bookCopy);
    }

    @PutMapping("/{bookCopyId}")
    public ResponseEntity<?> updateBookCopy(@PathVariable UUID bookCopyId, @RequestBody BookCopyUpdateDTO bookCopyDTO) {
        try {
            // Kiểm tra và lấy BookCopy
            BookCopy existingBookCopy = bookCopyService.getBookCopyById(bookCopyId);
            if (bookCopyDTO.getLocation() != null) {
                existingBookCopy.setLocation(bookCopyDTO.getLocation());
            }
            if (bookCopyDTO.getStatus() != null) {
                existingBookCopy.setStatus(bookCopyDTO.getStatus());
//                inventoryService.updateActionInventory(existingBookCopy.getBookId(), bookCopyDTO.getStatus());
            }
            // Lưu BookCopy sau khi cập nhật
            BookCopy updatedBookCopy = bookCopyService.addBookCopy(existingBookCopy);
            return ResponseEntity.ok(bookCopyMapper.mapToDto(updatedBookCopy));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "BookCopy not found with ID: " + bookCopyId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update BookCopy: " + e.getMessage()));
        }
    }


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

    @GetMapping("/by-book/{bookId}")
    public ResponseEntity<?> getBookCopiesByBookId(@PathVariable UUID bookId) {
        return ResponseEntity.ok(bookCopyService.findByBookId(bookId));
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<?> getBookCopiesByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(bookCopyService.findByStatus(status));
    }

    @DeleteMapping("/{bookCopyId}")
    public ResponseEntity<?> deleteBookCopyById(@PathVariable UUID bookCopyId) {
        try {
            bookCopyService.deleteBookCopyById(bookCopyId);
            return ResponseEntity.ok(Map.of("message", "Book copy deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    // API tổng số sách trong kho
    @GetMapping("/statistics/total")
    public ResponseEntity<Long> getTotalBooks() {
        return ResponseEntity.ok(bookCopyService.getTotalBooks());
    }

    // API tổng số sách đang mượn
    @GetMapping("/statistics/borrowed")
    public ResponseEntity<Long> getTotalBorrowedBooks() {
        return ResponseEntity.ok(bookCopyService.getTotalBorrowedBooks());
    }

    // API tổng số sách có sẵn
    @GetMapping("/statistics/available")
    public ResponseEntity<Long> getTotalAvailableBooks() {
        return ResponseEntity.ok(bookCopyService.getTotalAvailableBooks());
    }

    // API tổng số sách bị mất/hư hỏng
    @GetMapping("/statistics/lost-or-damaged")
    public ResponseEntity<Long> getTotalLostOrDamagedBooks() {
        return ResponseEntity.ok(bookCopyService.getTotalLostOrDamagedBooks());
    }
}
