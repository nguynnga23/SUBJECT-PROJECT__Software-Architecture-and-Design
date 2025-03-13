package com.example.bookservice.service;

import com.example.bookservice.entity.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookService {
    Book addBook(Book book);
    List<Book> getBooks();
    Book getBook(UUID bookID);
    boolean deleteBook(UUID bookID);
    Book updateBook(UUID bookID, Book book);
    boolean existsByBookCode(String bookCode);
    List<Book> searchBookByKeyword(String keyword);
    Optional<Book> findByBookCode(String bookCode);
}
