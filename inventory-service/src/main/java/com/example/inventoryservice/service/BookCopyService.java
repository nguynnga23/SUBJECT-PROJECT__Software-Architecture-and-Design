package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.BookCopy;

import java.util.List;

public interface BookCopyService {
    BookCopy addBookCopy(BookCopy bookCopy);
    List<BookCopy> getAllBookCopy();
    String findLatestCopyCode();
}
