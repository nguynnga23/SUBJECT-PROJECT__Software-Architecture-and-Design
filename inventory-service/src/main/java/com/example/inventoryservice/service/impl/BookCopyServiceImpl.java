package com.example.inventoryservice.service.impl;
import com.example.inventoryservice.entity.BookCopy;
import com.example.inventoryservice.enums.Status;
import com.example.inventoryservice.repositories.BookCopyRepository;
import com.example.inventoryservice.repositories.InventoryRepository;
import com.example.inventoryservice.service.BookCopyService;
import jakarta.persistence.EntityNotFoundException;
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
    public BookCopy getBookCopyById(UUID bookCopyId) {
        return bookCopyRepository.findById(bookCopyId).orElseThrow(EntityNotFoundException::new);
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

    @Override
    public void updateBookCopyStatus(UUID bookCopyId, Status status) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId).orElseThrow(EntityNotFoundException::new);
        bookCopy.setStatus(status);
        bookCopyRepository.save(bookCopy);
    }

    @Override
    public List<BookCopy> findByBookId(UUID bookId) {
        return bookCopyRepository.findByBookId(bookId);
    }

    @Override
    public List<BookCopy> findByStatus(Status status) {
        return bookCopyRepository.findByStatus(status);
    }

    @Override
    public void deleteBookCopyById(UUID bookCopyId) {
        if (!bookCopyRepository.existsById(bookCopyId)) {
            throw new EntityNotFoundException("BookCopy with ID " + bookCopyId + " not found.");
        }
        bookCopyRepository.deleteById(bookCopyId);
    }
    // Tổng số sách trong kho
    public Long getTotalBooks() {
        return bookCopyRepository.countTotalBooks();
    }

    // Tổng số sách đang mượn
    public Long getTotalBorrowedBooks() {
        return bookCopyRepository.countBorrowedBooks();
    }

    // Tổng số sách có sẵn
    public Long getTotalAvailableBooks() {
        return bookCopyRepository.countAvailableBooks();
    }

    // Tổng số sách bị mất/hư hỏng
    public Long getTotalLostOrDamagedBooks() {
        return bookCopyRepository.countLostOrDamagedBooks();
    }

}
