package com.example.bookservice.service.impl;

import com.example.bookservice.entity.Author;
import com.example.bookservice.entity.Book;
import com.example.bookservice.kafka.BookKafkaProducer;
import com.example.bookservice.repository.AuthorRepository;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookKafkaProducer kafkaProducer;

    @Override
    public Book addBook(Book book) {
        Book savedBook = bookRepository.save(book);
        kafkaProducer.sendBookCreatedEvent("BookCreated: " + savedBook.getId());
        return savedBook;
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
    public Book updateBook(UUID bookID, Book bookDetails) {
        return bookRepository.findById(bookID)
                .map(existingBook -> {
                    if (bookDetails.getBookCode() != null) {
                        existingBook.setBookCode(bookDetails.getBookCode());
                    }
                    if (bookDetails.getTitle() != null) {
                        existingBook.setTitle(bookDetails.getTitle());
                    }
                    if (bookDetails.getTopic() != null) {
                        existingBook.setTopic(bookDetails.getTopic());
                    }
                    if (bookDetails.getDescription() != null) {
                        existingBook.setDescription(bookDetails.getDescription());
                    }
                    if (bookDetails.getNote() != null) {
                        existingBook.setNote(bookDetails.getNote());
                    }
                    if (bookDetails.getCategory() != null) {
                        existingBook.setCategory(bookDetails.getCategory());
                    }
                    return bookRepository.save(existingBook);
                })
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookID));
    }

    @Override
    public boolean existsByBookCode(String bookCode) {
        return bookRepository.existsByBookCode(bookCode);
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword);
    }

    @Override
    public Optional<Book> findByBookCode(String bookCode) {
        return bookRepository.findByBookCode(bookCode);
    }

    @Override
    public Book addAuthorsToBook(UUID bookId, List<UUID> authorIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        List<Author> authors = authorRepository.findAllById(authorIds);

        if (authors.isEmpty()) {
            throw new RuntimeException("No valid authors found");
        }

        book.getAuthors().addAll(authors);
        return bookRepository.save(book);
    }
}
