package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.BookCopyRequest;
import com.example.inventoryservice.entity.BookCopy;
import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.service.BookCopyService;
import com.example.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BookCopyService bookCopyService;

    @PostMapping("/add")
    public ResponseEntity<BookCopy> addBookCopy(@RequestBody BookCopyRequest request) {
        BookCopy newCopy = bookCopyService.addBookCopy(request);
        return ResponseEntity.ok(newCopy);
    }

}
