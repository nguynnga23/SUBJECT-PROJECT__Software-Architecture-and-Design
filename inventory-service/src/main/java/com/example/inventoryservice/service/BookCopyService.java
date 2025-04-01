package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.BookCopyRequest;
import com.example.inventoryservice.entity.BookCopy;

public interface BookCopyService {
    BookCopy addBookCopy(BookCopyRequest bookCopyRequest);
}
