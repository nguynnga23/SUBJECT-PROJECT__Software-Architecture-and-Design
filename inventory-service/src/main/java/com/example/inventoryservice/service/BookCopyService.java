package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.BookCopy;

public interface BookCopyService {
    BookCopy addBookCopy(BookCopy bookCopy);
    String findLatestCopyCode();
}
