package com.example.bookservice.controller;


import com.example.bookservice.dto.BookRequestDTO;
import com.example.bookservice.entity.Author;
import com.example.bookservice.entity.Book;
import com.example.bookservice.entity.Category;
import com.example.bookservice.service.AuthorService;
import com.example.bookservice.service.BookService;
import com.example.bookservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/book-service/books")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody BookRequestDTO request) {
        if (bookService.existsByBookCode(request.bookCode())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Book code is exist"));
        }

        Book book = new Book();
        book.setBookCode(request.bookCode());
        book.setTitle(request.title());
        book.setTopic(request.topic());
        book.setDescription(request.description());
        book.setNote(request.note());
        book.setYearPublished(request.yearPublished());
        book.setPublisher(request.publisher());

        Optional<Category> category = categoryService.getCategoryById(request.categoryId());
        category.ifPresent(book::setCategory);

        Book savedBook = bookService.addBook(book);
        return ResponseEntity.ok(savedBook);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam(name = "query", required = false) String title) {
        List<Book> books = bookService.searchBooks(title);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable UUID bookId){
        try{
            Book book = bookService.getBook(bookId);
            return ResponseEntity.ok(book);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("error", "bookId is not exist"));
        }
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable UUID bookId, @RequestBody BookRequestDTO request){
        Optional<Book> existingBook = bookService.findByBookCode(request.bookCode());

        if (existingBook.isPresent() && !existingBook.get().getId().equals(bookId)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Book code already exists for another book"));
        }
        Book updateBook = new Book();
        updateBook.setBookCode(request.bookCode());
        updateBook.setTitle(request.title());
        updateBook.setTopic(request.topic());
        updateBook.setDescription(request.description());
        updateBook.setNote(request.note());
        updateBook.setYearPublished(request.yearPublished());
        updateBook.setPublisher(request.publisher());

        Optional<Category> category = categoryService.getCategoryById(request.categoryId());
        category.ifPresent(updateBook::setCategory);
        try{
            return ResponseEntity.ok(bookService.updateBook(bookId, updateBook));
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable UUID bookId){
        boolean isDeleted = bookService.deleteBook(bookId);

        if(isDeleted){
            return ResponseEntity.ok(Map.of("message", "Book deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Book not found"));
        }
    }

    @PostMapping("/{bookId}/authors")
    public ResponseEntity<?> addAuthorsToBook(@PathVariable UUID bookId, @RequestBody List<UUID> authorIds) {
        try {
            Book updatedBook = bookService.addAuthorsToBook(bookId, authorIds);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
