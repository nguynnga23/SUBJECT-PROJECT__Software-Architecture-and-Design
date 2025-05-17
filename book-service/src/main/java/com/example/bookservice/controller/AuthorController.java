package com.example.bookservice.controller;

import com.example.bookservice.dto.AuthorDTO;
import com.example.bookservice.dto.CategoryDTO;
import com.example.bookservice.entity.Author;
import com.example.bookservice.entity.Category;
import com.example.bookservice.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @PostMapping
    public ResponseEntity<?> addAuthor(@RequestBody CategoryDTO category) {

        Author savedAuthor = new Author();
        savedAuthor.setName(category.name());

        return ResponseEntity.ok(authorService.addAuthor(savedAuthor));
    }

    @PutMapping("/{author_id}")
    public ResponseEntity<?> updateAuthor(@PathVariable UUID author_id, @RequestBody AuthorDTO author) {
        Author savedAuthor = authorService.getAuthorById(author_id).get();
        savedAuthor.setName(author.name());
        return ResponseEntity.ok(authorService.updateAuthor(savedAuthor));
    }

    @DeleteMapping("/{author_id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable UUID auhtor_id) {
        boolean isDeleted = authorService.deleteAuthor(auhtor_id);

        if(isDeleted){
            return ResponseEntity.ok(Map.of("message", "Author deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Author not found"));
        }
    }
}
