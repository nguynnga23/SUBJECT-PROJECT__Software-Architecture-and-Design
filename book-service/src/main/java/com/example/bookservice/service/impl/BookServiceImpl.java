package com.example.bookservice.service.impl;

import com.example.bookservice.entity.Book;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBook(UUID bookID) {
        return bookRepository.findById(bookID).get();
    }

    @Override
    public boolean deleteBook(UUID bookID) {
        if (bookRepository.existsById(bookID)) {
            bookRepository.deleteById(bookID);
            return true;
        }
        return false;
    }

    @Override
    public Book updateBook(UUID bookID, Book book) {
        if(!bookRepository.existsById(bookID)) {
            throw new RuntimeException("Book does not exist");
        }
        book.setBookId(bookID);
        Book updatedBook = bookRepository.save(book);
        return updatedBook;
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    @Override
    public List<Book> searchBookByKeyword(String keyword) {
        return bookRepository.searchByKeyword(keyword);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
}
