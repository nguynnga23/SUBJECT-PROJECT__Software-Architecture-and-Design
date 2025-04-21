package com.example.inventoryservice.service.impl;
import com.example.inventoryservice.entity.BookCopy;
import com.example.inventoryservice.enums.Status;
import com.example.inventoryservice.repositories.BookCopyRepository;
import com.example.inventoryservice.repositories.InventoryRepository;
import com.example.inventoryservice.service.BookCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookCopyServiceImpl implements BookCopyService {
    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Override
    public BookCopy addBookCopy(BookCopy bookCopy) {
        return bookCopyRepository.save(bookCopy);
    }

    @Override
    public List<BookCopy> getAllBookCopy() {
        return bookCopyRepository.findAll();
    }

    @Override
    public String findLatestCopyCode() {
        return bookCopyRepository.findLatestCopyCode();
    }

    @Override
    public BookCopy findFirstByBookIdAndStatus(UUID bookId, Status status) {
        return bookCopyRepository.findFirstByBookIdAndStatus(bookId, Status.AVAILABLE);
    }


}
