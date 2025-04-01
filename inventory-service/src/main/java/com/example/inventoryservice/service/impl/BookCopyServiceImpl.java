package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.dto.BookCopyRequest;
import com.example.inventoryservice.entity.BookCopy;
import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.enums.Status;
import com.example.inventoryservice.repositories.BookCopyRepository;
import com.example.inventoryservice.repositories.InventoryRepository;
import com.example.inventoryservice.service.BookCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookCopyServiceImpl implements BookCopyService {
    @Autowired
    private BookCopyRepository bookCopyRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public BookCopy addBookCopy(BookCopyRequest request) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(request.inventoryId());
        if (inventoryOpt.isEmpty()) {
            throw new RuntimeException("Inventory không tồn tại!");
        }

        BookCopy bookCopy = new BookCopy();
        bookCopy.setBookId(request.bookId());
        bookCopy.setLocation(request.location());
        bookCopy.setCopyCode(request.copyCode());
        bookCopy.setStatus(Status.AVAILABLE);
        bookCopy.setInventory(inventoryOpt.get());

        return bookCopyRepository.save(bookCopy);
    }



}
