package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.BookCopy;
import com.example.inventoryservice.enums.Status;

import java.util.List;
import java.util.UUID;

public interface BookCopyService {
    BookCopy addBookCopy(BookCopy bookCopy);
    List<BookCopy> getAllBookCopy();
    String findLatestCopyCode();
    BookCopy findFirstByBookIdAndStatus(UUID bookId, Status status);
}
