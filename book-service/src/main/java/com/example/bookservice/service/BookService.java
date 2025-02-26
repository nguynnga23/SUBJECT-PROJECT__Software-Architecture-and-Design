package com.example.bookservice.service;

import com.example.bookservice.entity.Book;

import java.util.List;
import java.util.UUID;

public interface BookService {
    Book addBook(Book book);
    List<Book> getBooks();
    Book getBook(UUID bookID);
    boolean deleteBook(UUID bookID);
    Book updateBook(UUID bookID, Book book);
    boolean existsByIsbn(String isbn);
    List<Book> searchBookByKeyword(String keyword);
}
