package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.BookCopy;
import com.example.inventoryservice.service.BookCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory-service/copies")
public class BookCopyController {

    @Autowired
    private BookCopyService bookCopyService;

    @PostMapping
    public ResponseEntity<BookCopy> addBookCopy(@RequestBody BookCopy bookCopy) {
        BookCopy savedBookCopy = bookCopyService.addBookCopy(bookCopy);
        return ResponseEntity.ok(savedBookCopy);
    }

    @GetMapping("/latest-copy-code")
    public ResponseEntity<String> getLatestCopyCode() {
        String latestCopyCode = bookCopyService.findLatestCopyCode();
        return ResponseEntity.ok(latestCopyCode);
    }
}
