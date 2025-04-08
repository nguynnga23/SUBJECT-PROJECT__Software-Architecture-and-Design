package com.example.inventoryservice.service.impl;
import com.example.inventoryservice.entity.BookCopy;
import com.example.inventoryservice.repositories.BookCopyRepository;
import com.example.inventoryservice.repositories.InventoryRepository;
import com.example.inventoryservice.service.BookCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookCopyServiceImpl implements BookCopyService {
    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Override
    public BookCopy addBookCopy(BookCopy bookCopy) {
        return bookCopyRepository.save(bookCopy);
    }

    @Override
    public String findLatestCopyCode() {
        return bookCopyRepository.findLatestCopyCode();
    }


}
