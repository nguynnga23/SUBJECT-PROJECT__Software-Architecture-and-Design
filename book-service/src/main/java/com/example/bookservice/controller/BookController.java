package com.example.bookservice.controller;


import com.example.bookservice.entity.Book;
import com.example.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        if (bookService.existsByIsbn(book.getIsbn())) {
            return ResponseEntity.badRequest().body(Map.of("error", "ISBN is exist"));
        }
        Book newBook = bookService.addBook(book);
        return ResponseEntity.ok(newBook);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Book>> getBooksByKeyword(@PathVariable("keyword") String keyword) {
        List<Book> books = bookService.searchBookByKeyword(keyword);
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
    public ResponseEntity<?> updateBook(@PathVariable UUID bookId, @RequestBody Book updateBook){
        Optional<Book> existingBook = bookService.findByIsbn(updateBook.getIsbn());

        if (existingBook.isPresent() && !existingBook.get().getBookId().equals(bookId)) {
            return ResponseEntity.badRequest().body(Map.of("error", "ISBN already exists for another book"));
        }
        
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
}
